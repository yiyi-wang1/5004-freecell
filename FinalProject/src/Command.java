/**
 * This is Command interface with execute and undo methods.
 * @author Yiyi Wang
 *
 */
public interface Command {
	/**
	 * This method will execute the current Command
	 */
	void execute();
	
	/**
	 * This method will undo the current Command
	 */
	void undo();

}
