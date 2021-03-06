/*
 * Copyright 2018 Continuous Performance Test
 * 
 * 
 * This file is part of Continuous Performance Test.
 * https://github.com/continuousperftest/agent-java
 * 
 * Continuous Performance Test is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 * 
 * Continuous Performance Test is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Continuous
 * Performance Test. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.continuousperftest.service;

import com.github.continuousperftest.service.impl.LocalMetricExporterServiceImpl;
import com.github.continuousperftest.service.impl.RemoteMetricExporterServiceImpl;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Creates a metric exporter service instance based on a parameter.
 * 
 * @author Aleh Struneuski
 */
public class MetricExporterFactory {

  public static MetricExporterService createExporter(MetricExporter metricEporter) {
    switch (metricEporter) {
      case LOCAL:
        return new LocalMetricExporterServiceImpl();
      case REMOTE:
        return new RemoteMetricExporterServiceImpl();
      case OPTED:
        MetricExporterService optedMetricExporter = loadMetricExporterServiceImpl();
        if (optedMetricExporter != null) {
          return optedMetricExporter;
        } else {
          throw new RuntimeException(
              "Specified implementation of MetricExporterService was not found!!!");
        }
      default:
        return new LocalMetricExporterServiceImpl();
    }
  }

  private static MetricExporterService loadMetricExporterServiceImpl() {
    MetricExporterService opted = null;
    ServiceLoader<MetricExporterService> loader = ServiceLoader.load(MetricExporterService.class);
    Iterator<MetricExporterService> metricExporters = loader.iterator();
    if (metricExporters.hasNext()) {
      opted = metricExporters.next();
    }
    return opted;
  }
}
