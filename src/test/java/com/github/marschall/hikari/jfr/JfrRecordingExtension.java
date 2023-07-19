package com.github.marschall.hikari.jfr;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import jdk.jfr.EventType;
import jdk.jfr.Recording;
import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordingFile;

final class JfrRecordingExtension implements BeforeAllCallback, AfterAllCallback {

  private volatile Path recordingLocation;
  private volatile Recording recording;

  JfrRecordingExtension() {
  }

  @Override
  public void beforeAll(ExtensionContext context) throws Exception {
    this.recordingLocation = this.computeRecordingPath(context);

    this.startRecording();
  }

  @Override
  public void afterAll(ExtensionContext context) throws Exception {
    this.stopRecording();
    this.readRecording();
  }

  private Path computeRecordingPath(ExtensionContext context) {
    Class<?> testClass = context.getRequiredTestClass();
    return Path.of("target", testClass.getSimpleName() + ".jfr");
  }

  private void startRecording() throws IOException {
    this.recording = new Recording();
    this.recording.enable(JFRMetricsTracker.ConnectionCreatedEvent.class);
    this.recording.enable(JFRMetricsTracker.ConnectionAcquiredEvent.class);
    this.recording.enable(JFRMetricsTracker.ConnectionBorrowedEvent.class);
    this.recording.enable(JFRMetricsTracker.ConnectionTimeoutEvent.class);
    this.recording.enable("org.junit.TestExecution");
    this.recording.enable("org.junit.TestPlan");
    this.recording.setMaxSize(1L * 1024L * 1024L);
    this.recording.setToDisk(true);
    this.recording.setDestination(this.recordingLocation);
    this.recording.start();
  }

  private void stopRecording() throws IOException {
    this.recording.close();
  }

  private void readRecording() throws IOException {
    int eventCount = 0;
    try (RecordingFile recordingFile = new RecordingFile(this.recordingLocation)) {
      while (recordingFile.hasMoreEvents()) {
        RecordedEvent event = recordingFile.readEvent();
        EventType eventType = event.getEventType();
        if (eventType.getName().startsWith(JFRMetricsTracker.class.getName())) {
          eventCount += 1;
        }
      }
    }
    assertTrue(eventCount > 0);
  }

}
