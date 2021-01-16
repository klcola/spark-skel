
// 项目名称，可以替换成你自己的项目名称
name := "spark-skel"

// 软件版本
version := "1.0"

// 所使用的 scala 版本，注意 spark 3.0.0 仅支持 scala-2.12.x
scalaVersion := "2.12.13"

// 项目所使用的 spark 版本号，spark 最新版本列表详见 http://spark.apache.org/downloads.html
val sparkVersion = "3.0.0"


// 每行末尾的 "provided" 字符串表示该依赖包会由 spark 运行环境提供，在 sbt assembly 时不会被包含在打好的
// 项目 jar 文件中，这样可以有效的缩小项目 jar 文件的大小。因此，如果确信 spark 运行时环境会提供相关 jar 包，
// 请尽量在该依赖行后加 "provide" 或者 "test" 来确保该依赖相关的文件不会包含在生成的项目 jar 文件中。
// 当然如果你使用 sbt package 命令来编译项目并打包，无论依赖行后是否有 "provided" 字符串，该依赖都不会被包
// 含到项目 jar 文件中。
libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
    "org.apache.spark" %% "spark-mllib" % sparkVersion % "provided",
    "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
    "org.apache.spark" %% "spark-avro" % sparkVersion
)

// 在使用 sbt package 编译打包时 jar 文件的命名规则，jar 文件生成后会在 target/scala-2.12/ 文件夹中
artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
    artifact.name + "_" + sv.binary + "-" + sparkVersion + "_" + module.revision + "." + artifact.extension
}

// 在使用 sbt assembly 编译打包时，无需包含 scala 依赖，因为 spark 运行时环境总会提供 scala 依赖。
assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)

// 在使用 sbt assembly 编译打包时 jar 文件的命名规则，jar 文件生成后会在 target/scala-2.12/ 文件夹中
assemblyJarName in assembly := s"${name.value}_${scalaVersion.value}-${sparkVersion}_${version.value}.jar"

// spark-avro 和 spark-tags 中有相同类名冲突，通过如下配置解决
assemblyMergeStrategy in assembly := {
    case PathList(path @ _*) if path.last endsWith "UnusedStubClass.class" => MergeStrategy.first
    case x => {
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    }
}