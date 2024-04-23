import org.typelevel.scalacoptions.ScalacOptions
import sbt.Keys.libraryDependencies

val scala3Version = "3.4.1"

val CatsVersion       = "2.10.0"
val CatsEffectVersion = "3.2.9"
val Fs2Version        = "3.10.2"
val Http4sVersion     = "0.23.26"
val CirceVersion      = "0.14.6"
val ScodecVersion     = "2.2.2"

val Log4catsVersion        = "2.6.0"
val SkunkVersion           = "0.6.3"
val Redis4catsVersion      = "1.7.0"
val LogbackVersion         = "1.5.6"
val MunitVersion           = "0.7.29"
val MunitCatsEffectVersion = "1.0.7"

val Endless4sVersion            = "0.30.0"
val Endless4sTransactionVersion = "0.1.0"
val EdomataVersion              = "0.12.1"

val Http4sSessionVersion = "0.2.0"
val Http4sArmeriaVersion = "0.5.3"

val DisciplineVersion      = "1.6.0"
val MunitDisciplineVersion = "2.0.0-M2"

lazy val root = (project in file("."))
  .enablePlugins(Http4sGrpcPlugin, JavaAppPackaging, AshScriptPlugin)
  .settings(
    organization        := "blackwhirlwind.game",
    name                := "gameshell",
    version             := "0.0.1-SNAPSHOT",
    fork                := true,
    scalaVersion        := scala3Version,
    Compile / mainClass := Some("blackwhirlwind.game.gameshell.Main"),
    dockerExposedPorts ++= Seq(8558, 8080),
    dockerBaseImage := "docker.io/library/eclipse-temurin:21-jre-alpine",
    libraryDependencies ++= Seq(
      // cats
      "org.typelevel" %% "cats-core" % CatsVersion,

      // cats effect
      "org.typelevel" %% "cats-effect" % CatsEffectVersion,

      // fs2
      "co.fs2" %% "fs2-core"   % Fs2Version,
      "co.fs2" %% "fs2-io"     % Fs2Version,
      "co.fs2" %% "fs2-scodec" % Fs2Version,

      // http4s
      "org.http4s" %% "http4s-ember-server" % Http4sVersion,
      "org.http4s" %% "http4s-ember-client" % Http4sVersion,
      "org.http4s" %% "http4s-circe"        % Http4sVersion,
      "org.http4s" %% "http4s-dsl"          % Http4sVersion,

      // Optional for auto-derivation of JSON codecs
      "io.circe" %% "circe-generic" % CirceVersion,
      // Optional for string interpolation to JSON model
      "io.circe" %% "circe-literal" % CirceVersion,

      // binary
      "org.scodec" %% "scodec-core" % ScodecVersion,

      // http4s 3rd party
      "org.http4s" %% "http4s-session" % Http4sSessionVersion,
      // armeria
      // For server
      "org.http4s" %% "http4s-armeria-server" % Http4sArmeriaVersion,
      // For client
      "org.http4s" %% "http4s-armeria-client" % Http4sArmeriaVersion,

      // endless4s
      "io.github.endless4s" %% "endless-core"             % Endless4sVersion,
      "io.github.endless4s" %% "endless-runtime-pekko"    % Endless4sVersion,
      "io.github.endless4s" %% "endless-runtime-akka"     % Endless4sVersion,
      "io.github.endless4s" %% "endless-protobuf-helpers" % Endless4sVersion,
      //      "io.github.endless4s" %% "endless-scodec-helpers" % Endless4sVersion,
      "io.github.endless4s" %% "endless-circe-helpers"     % Endless4sVersion,
      "io.github.endless4s" %% "endless-transaction"       % Endless4sTransactionVersion,
      "io.github.endless4s" %% "endless-transaction-pekko" % Endless4sTransactionVersion,
      "io.github.endless4s" %% "endless-transaction-akka"  % Endless4sTransactionVersion,
      // edomata
      "dev.hnaderi" %% "edomata-core" % EdomataVersion,

      // log4cats
      "org.typelevel" %% "log4cats-slf4j" % Log4catsVersion,
      // logback
      "ch.qos.logback" % "logback-classic" % LogbackVersion,

      // skunk
      "org.tpolecat" %% "skunk-core" % SkunkVersion,

      // redis
      "dev.profunktor" %% "redis4cats-effects"  % Redis4catsVersion,
      "dev.profunktor" %% "redis4cats-streams"  % Redis4catsVersion,
      "dev.profunktor" %% "redis4cats-log4cats" % Redis4catsVersion,

      // grpc (optional?)
      "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",

      // law enforcement
      "org.typelevel" %% "discipline-core" % DisciplineVersion,

      // Test
      "org.scalameta"  %% "munit"               % MunitVersion           % Test,
      "org.typelevel"  %% "munit-cats-effect-3" % MunitCatsEffectVersion % Test,
      "org.typelevel" %%% "discipline-munit"    % MunitDisciplineVersion % Test
    ),
    assembly / assemblyMergeStrategy := {
      case "module-info.class" => MergeStrategy.discard
      case x                   => (assembly / assemblyMergeStrategy).value.apply(x)
    },
    Compile / PB.targets ++= Seq(
      // set grpc = false because http4s-grpc generates its own code
      scalapb.gen(grpc = false, scala3Sources = true) -> (Compile / sourceManaged).value / "scalapb"
    ),
    // grpc plugin will generate discard value warnings
    Compile / tpolecatExcludeOptions += ScalacOptions.fatalWarnings
  )
