package com.oreilly.spock.extension;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import org.spockframework.runtime.AbstractRunListener;
import org.spockframework.runtime.extension.ExtensionException;
import org.spockframework.runtime.model.BlockInfo;
import org.spockframework.runtime.model.BlockKind;
import org.spockframework.runtime.model.FeatureInfo;
import org.spockframework.runtime.model.SpecInfo;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static org.spockframework.runtime.model.BlockKind.*;

// tag::run-listener[]
class GherkinListener extends AbstractRunListener {

  private final Writer writer;

  GherkinListener(Writer writer) {
    this.writer = writer;
  }

  @Override public void beforeSpec(SpecInfo spec) { // <1>
    writeFeature(spec);
    writeNarrative(spec);
  }

  @Override public void beforeFeature(FeatureInfo feature) { // <2>
    writeScenario(feature);
    feature
      .getBlocks()
      .stream()
      .filter(this::isSupportedBlockType)
      .forEach(this::beforeBlock);
  }

  @Override public void afterFeature(FeatureInfo feature) { // <3>
    appendLine("");
  }

  @Override public void afterSpec(SpecInfo spec) { // <4>
    try {
      writer.close();
    } catch (IOException e) {
      throw new ExtensionException("Cannot close writer", e);
    }
  }

  private void beforeBlock(BlockInfo block) {
    List<String> lines = block.getTexts(); // <5>
    if (!lines.isEmpty()) {
      writeStep(block.getKind(), lines.get(0));
      lines
        .subList(1, lines.size())
        .forEach(this::writeAndStep);
    } else {
      writeStep(block.getKind(), "DESCRIPTION MISSING");
    }
  }

  private void writeFeature(SpecInfo spec) {
    appendLine("Feature: %s", spec.getName());
  }

  private void writeNarrative(SpecInfo spec) {
    String narrative = spec.getNarrative(); // <6>
    if (narrative != null) {
      stream(narrative.split("\n"))
        .forEach(line -> appendLine("\t%s", line));
    }
    appendLine("");
  }

  private void writeScenario(FeatureInfo feature) {
    appendLine("\tScenario: %s", feature.getName());
  }

  private void writeStep(BlockKind blockKind, String description) {
    String keyword = keywordFor(blockKind);
    appendLine("\t\t%s %s", keyword, description);
  }

  private void writeAndStep(String description) {
    appendLine("\t\t\tAnd %s", description);
  }

  private boolean isSupportedBlockType(BlockInfo block) {
    List<BlockKind> supported = Arrays.asList(SETUP, WHEN, THEN, EXPECT);
    return supported.contains(block.getKind());
  }

  private String keywordFor(BlockKind block) {
    switch (block) {
      case SETUP:
        return "Given";
      case WHEN:
        return "When";
      case THEN:
      case EXPECT:
        return "Then";
      default:
        throw new IllegalArgumentException(format("Block kind %s not supported", block));
    }
  }

  private void appendLine(String format, Object... args) {
    try {
      writer
        .append(format(format, args))
        .append("\n");
    } catch (IOException e) {
      throw new ExtensionException("Unable to write", e);
    }
  }
}
// end::run-listener[]
