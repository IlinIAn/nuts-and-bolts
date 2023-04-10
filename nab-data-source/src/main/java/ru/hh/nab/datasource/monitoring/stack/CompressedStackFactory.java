package ru.hh.nab.datasource.monitoring.stack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Only middle part of stack trace is retained, from inner class and method to outer class and method, both parts excluding.<br/>
 * Only stack frames from the includePackages are kept.<br/>
 * But if a class contains one of the excludeClassesParts, the stack frame is excluded even if it belongs to one of the includePackages.
 */
public class CompressedStackFactory {
  private final CompressedStackFactoryConfig config;

  public CompressedStackFactory(CompressedStackFactoryConfig config) {
    this.config = config;
  }

  public String create() {
    StackTraceElement[] frames = Thread.currentThread().getStackTrace();
    int frameIndex = findStartIndex(frames);

    List<String> shortFrames = new ArrayList<>();
    String prevClass = null;
    for (; frameIndex < frames.length; frameIndex++) {
      StackTraceElement frame = frames[frameIndex];
      String className = frame.getClassName();
      String methodName = frame.getMethodName();

      if (className.equals(config.getOuterClassExcluding())
          && methodName.equals(config.getOuterMethodExcluding())) {
        break;
      }

      if (!isFromIncludePackages(className) || isFromExcludeClassesParts(className)) {
        continue;
      }

      className = removeEnhancerPostfix(className);

      // sacrifice frames of the same class
      if (className.equals(prevClass)) {
        continue;
      }
      prevClass = className;

      String shortClassName = className.substring(className.lastIndexOf('.') + 1);
      shortFrames.add(shortClassName + '.' + methodName);
    }
    Collections.reverse(shortFrames);
    return String.join(">", shortFrames);
  }

  private int findStartIndex(StackTraceElement[] frames) {
    for (int frameIndex = 0; frameIndex < frames.length; frameIndex++) {
      StackTraceElement frame = frames[frameIndex];
      if (frame.getClassName().equals(config.getInnerClassExcluding())
          && frame.getMethodName().equals(config.getInnerMethodExcluding())) {
        return frameIndex + 1;
      }
    }
    return 1;  // the one, that called the create method
  }

  private boolean isFromIncludePackages(String className) {
    for (String includePackage : config.getIncludePackages()) {
      if (className.startsWith(includePackage)) {
        return true;
      }
    }
    return false;
  }

  private boolean isFromExcludeClassesParts(String className) {
    for (String excludeClassPart : config.getExcludeClassesParts()) {
      if (className.contains(excludeClassPart)) {
        return true;
      }
    }
    return false;
  }

  private String removeEnhancerPostfix(String className) {
    int indexOfSS = className.indexOf("$$");
    if (indexOfSS > 0) {
      return className.substring(0, indexOfSS);
    } else {
      return className;
    }
  }
}
