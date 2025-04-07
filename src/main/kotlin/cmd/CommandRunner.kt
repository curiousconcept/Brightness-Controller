package cmd

object CommandRunner {

    fun runCommand(command: String): String {
        try {
            val process = ProcessBuilder(*command.split(" ").toTypedArray())
                .redirectErrorStream(true)
                .start()

            val output = process.inputStream.bufferedReader().readText()

            return output

        } catch (e: Exception) {
            println("cmd.CommandRunner: Error executing command: ${e.message}")
            throw e // Re-throw the exception so the caller can handle it
        }
    }
}