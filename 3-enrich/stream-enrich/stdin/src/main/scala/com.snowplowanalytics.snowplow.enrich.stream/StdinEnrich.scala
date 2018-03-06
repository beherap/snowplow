/*
 * Copyright (c) 2013-2017 Snowplow Analytics Ltd.
 * All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache
 * License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at
 * http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied.
 *
 * See the Apache License Version 2.0 for the specific language
 * governing permissions and limitations there under.
 */
package com.snowplowanalytics
package snowplow
package enrich
package stream

import java.io.File
import java.net.URI

import scalaz.{Sink => _, Source => _, _}

import common.enrichments.EnrichmentRegistry
import config.FileConfig
import iglu.client.Resolver
import model.{Credentials, StreamsConfig}
import scalatracker.Tracker
import sources.{Source, StdinSource}

/** The main entry point for Stream Enrich for stdin/out. */
object StdinEnrich extends Enrich {

  def main(args: Array[String]): Unit = run(args)

  override def getSource(
    streamsConfig: StreamsConfig,
    resolver: Resolver,
    enrichmentRegistry: EnrichmentRegistry,
    tracker: Option[Tracker]
  ): Validation[String, Source] =
    StdinSource.create(streamsConfig, resolver, enrichmentRegistry, tracker)

  override val parser: scopt.OptionParser[FileConfig] = localParser

  override def download(uri: URI, targetFile: File)(
      implicit creds: Credentials): Validation[String, Int] =
    httpDownloader(uri, targetFile)

  override def extractResolver(resolverArgument: String)(
      implicit creds: Credentials): Validation[String, String] =
    localResolverExtractor(resolverArgument)

  override def extractEnrichmentConfigs(enrichmentArg: Option[String])(
      implicit creds: Credentials): Validation[String, String] =
    localEnrichmentConfigsExtractor(enrichmentArg)
}