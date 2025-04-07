import cmd.CommandRunner

private const val DDCUTIL = "ddcutil"
private const val BRIGHTNESS_VALUE = "0x10"
private const val REGEX_BRIGHTNESS = """VCP code 0x10 \(Brightness\s*\): current value =\s*(\d+)"""


class DccBrightnessControllerImpl : BrightnessController {


    private val dccCmdSet: String = "$DDCUTIL setvcp $BRIGHTNESS_VALUE";
    private val dccCmdGet: String = "$DDCUTIL getvcp $BRIGHTNESS_VALUE";

    override fun updateBrightness(value: Int) {
        CommandRunner.runCommand(dccCmdSet.plus(" ").plus(value))
    }

    override fun getCurrentBrightness(): Int {


        println(dccCmdGet)

        val output = CommandRunner.runCommand(dccCmdGet)

        println(output)

        return parseBrightness(output);
    }

    private fun parseBrightness(output: String): Int {
        // Define a regular expression to capture the current value
        val regex = REGEX_BRIGHTNESS.toRegex()

        // Find the match
        val matchResult = regex.find(output)

        return matchResult?.groups?.get(1)?.value?.toInt() ?: throw IllegalArgumentException("Could not extract brightness value from the output.")
    }
}