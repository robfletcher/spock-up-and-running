package tx

import org.skife.jdbi.v2.DBI
import org.skife.jdbi.v2.tweak.HandleCallback
import org.skife.jdbi.v2.util.DoubleColumnMapper
import spock.lang.AutoCleanup
import spock.lang.Specification
import squawker.mentions.TableHelper

class BasicTxSpec extends Specification implements TableHelper {

  @AutoCleanup def handle = DBI.open("jdbc:h2:mem:test")

  def setup() {
    handle.execute """
        create table inventory (id int auto_increment, name varchar(50));
        create table prices (inv_id int, currency char(3), price decimal);
      """
    handle.begin()
    handle.execute """
        -- tag::tx-example[]
        insert into inventory (name) values ('Tricorder');
        insert into prices (inv_id, currency, price)
                    select id, 'USD', 199.95
                      from inventory
                     where name = 'Tricorder';
        insert into prices (inv_id, currency, price)
                    select id, 'GBP', 154.2
                      from inventory
                     where name = 'Tricorder';
        insert into prices (inv_id, currency, price)
                    select id, 'ZAR', 2747.02
                      from inventory
                     where name = 'Tricorder';
        -- end::tx-example[]
      """
    handle.commit()
  }

  def "data was inserted"() {
    expect:
    count("inventory") == 1
    count("prices") == 3
    handle
      .createQuery("""
        select price from inventory i, prices p 
         where i.name = 'Tricorder' 
           and p.currency = 'USD' 
           and p.inv_id = i.id
       """)
      .map(DoubleColumnMapper.PRIMITIVE)
      .first() == 199.95
  }

  @Override
  def <R> R withHandle(HandleCallback<R> closure) {
    closure.withHandle(handle)
  }
}
