package com.oreilly.spock.extension;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import org.spockframework.runtime.extension.AbstractGlobalExtension;
import org.spockframework.runtime.extension.ExtensionException;
import org.spockframework.runtime.model.SpecInfo;
import static java.lang.String.format;

// tag::global-extension[]
public class GherkinExtension extends AbstractGlobalExtension {

  @Override public void visitSpec(SpecInfo spec) {
    File file = new File(format(
      "./build/reports/gherkin/%s.%s.feature",
      spec.getPackage(),
      spec.getName()
    ));

    try {
      file.getParentFile().mkdirs();
      Writer writer = new FileWriter(file);
      spec.addListener(new GherkinListener(writer));
    } catch (IOException e) {
      throw new ExtensionException("Error writing to file", e);
    }
  }
}
// end::global-extension[]
