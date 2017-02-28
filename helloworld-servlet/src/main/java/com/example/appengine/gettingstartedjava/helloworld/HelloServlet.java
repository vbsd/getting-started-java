/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.appengine.gettingstartedjava.helloworld;

import com.google.cloud.dataflow.sdk.Pipeline;
import com.google.cloud.dataflow.sdk.io.TextIO;
import com.google.cloud.dataflow.sdk.options.DataflowPipelineOptions;
import com.google.cloud.dataflow.sdk.options.PipelineOptions;
import com.google.cloud.dataflow.sdk.options.PipelineOptionsFactory;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.dataflow.sdk.runners.BlockingDataflowPipelineRunner;

// [START example]
@SuppressWarnings("serial")
@WebServlet(name = "helloworld", value = "/dataflow_job_quohQu5p" )
public class HelloServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    PrintWriter out = resp.getWriter();
    String message = executeDataflowPipeline();
    out.println(message);
  }

  public static String executeDataflowPipeline() {
    DataflowPipelineOptions options = PipelineOptionsFactory.create()
            .as(DataflowPipelineOptions.class);
    options.setRunner(BlockingDataflowPipelineRunner.class);
    // CHANGE 1/3: Your project ID is required in order to run your pipeline on the Google Cloud.
    options.setProject("<project-id>");
    // CHANGE 2/3: Your Google Cloud Storage path is required for staging local files.
    options.setStagingLocation("gs://<bucket-id>/test-staging");

    // Create the Pipeline object with the options we defined above.
    Pipeline p = Pipeline.create(options);

    // This job just copies some data from one Google Storage location to another.
    // CHANGE 3/3: Choose your Google Storage input and output location.
    p.apply(TextIO.Read.from("gs://<bucket-id>/in/*"))
            .apply(TextIO.Write.to("gs://<bucket-id>/out/"));

    // Run the pipeline.
    p.run();
    return "Succeeded";
  }
}
// [END example]