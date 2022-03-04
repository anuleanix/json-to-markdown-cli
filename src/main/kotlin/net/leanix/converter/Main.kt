package net.leanix.converter

import com.github.ajalt.clikt.completion.completionOption
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.output.CliktHelpFormatter
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.file
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import net.leanix.converter.model.ConverterConfiguration
import java.io.File
import java.nio.file.Paths
import java.util.stream.Collectors


private const val LINE_BREAK = "\n"

class JsonToMarkDown : CliktCommand() {
    init {
        context { helpFormatter = CliktHelpFormatter(showDefaultValues = true) }
        completionOption()
    }

    private val path: File by option("-p", "--path", help = "The path to the json file").file(
        mustExist = true,
        mustBeReadable = true
    ).required()
    private val configFile: File by option(
        "-c",
        "--config-file",
        help = "The path to the config file"
    ).file(mustExist = true, mustBeReadable = true).default(File("../config/metro-retro-config.json"))
    private val output: File by option(
        "-o",
        "--output-file",
        help = "The path to the output file"
    ).file(mustExist = false, canBeDir = false).default(File("output.txt"))


    override fun run() {
        val gson = Gson()
        val itemType = object : TypeToken<List<ConverterConfiguration>>() {}.type
        val path = Paths.get("").toAbsolutePath().toString()
        println("Working Directory = $path")
        val path2 = System.getProperty("user.dir")

        println("Working Directory = $path2")

        val configString = configFile.readText()
        val config: List<ConverterConfiguration> =
            gson.fromJson(configString, itemType)
        println("Configuration file read ${configFile.name}")
        writeMarkdownJsonPath(config)

    }

    private fun writeMarkdownJsonPath(config: List<ConverterConfiguration>) {
        val jsonContext: DocumentContext = readAndParseJsonFile()
        println("Json file read ${path.name}")
        val content = config.stream().map { c ->
            val strings: List<String> = jsonContext.read(c.jsonPath)
            val body =
                strings.stream()
                    .map { "${c.markdown} ${it.replace(LINE_BREAK, " ").replace("  ", " ")}" }
                    .collect(Collectors.joining(LINE_BREAK))
            "## ${c.header}$LINE_BREAK$body"
        }.collect(Collectors.joining(LINE_BREAK))
        output.writeText(content)
        println("Output file written ${output.name}")

    }

    private fun readAndParseJsonFile(): DocumentContext {
        val json = path.readText()
        return JsonPath.parse(json)
    }

}

fun main(args: Array<String>) = JsonToMarkDown().main(args)
