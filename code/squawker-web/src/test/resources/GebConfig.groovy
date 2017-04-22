// tag::environments[]
// tag::chrome-imports[]
import io.github.bonigarcia.wdm.ChromeDriverManager
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver

// end::chrome-imports[]
environments {
  chrome {
    // tag::chromedriver[]
    ChromeDriverManager.getInstance().setup()
    driver = { new ChromeDriver() }
    // end::chromedriver[]
  }

  firefox {
    driver = { new FirefoxDriver() }
  }
}
// end::environments[]
