import java.util.Stack;
/**
 * This represent the CommandManager for all commands in freecell game.
 * @author Yiyi Wang
 *
 */
public class CommandManager {
	private Stack<Command> undo = new Stack<Command>();

	/**
	 * This method will execute the command.
	 * @param c: the command to execute
	 */
	public void executeCommand(Command c) {
		c.execute();
		undo.push(c);//adding the command (with the state of model) to undo stack
	}
	
	/**
	 * This method will check if the command can be undo or not.
	 * @return false if the stack is empty
	 */
	public boolean undoable() {
		if(undo.isEmpty()) {
			return false;
		}else {
			return true;
		}
	}
	
	/**
	 * This method will get the latest command from undo stack and call its undo method.
	 */
	public void undo() {
		if(undoable()) {
			Command command = undo.pop();
			command.undo();
		}
	}
	
	/**
	 * This method will clear all command in stack for next game.
	 */
	public void clear() {
		undo.clear();
	}
}