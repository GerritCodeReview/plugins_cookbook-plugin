package com.googlesource.gerrit.plugins.cookbook;

import com.google.gerrit.extensions.events.UsageDataPublishedListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsageDataLogger implements UsageDataPublishedListener{

  private static final Logger log = LoggerFactory.getLogger(UsageDataLogger.class);

  @Override
  public void onUsageDataPublished(Event event) {
    if(log.isInfoEnabled()) {
      log.info(String.format("Usage data for "
          + "%s at %s", event.getMetaData().getDescription(), event.getInstant()));
      log.info(String.format("project name - %s", event.getMetaData().getName()));
      String unitSymbol = event.getMetaData().getUnitSymbol();
      for (Data data : event.getData()) {
        log.info(String.format("%s - %d %s", data.getProjectName(), data.getValue(), unitSymbol));
      }
      log.info("");
    }
  }

}
