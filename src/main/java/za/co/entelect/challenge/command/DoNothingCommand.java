package za.co.entelect.challenge.command;

public class DoNothingCommand implements Command {
    @Override
    public String render() {
        return String.format("NOTHING");
    }
}
