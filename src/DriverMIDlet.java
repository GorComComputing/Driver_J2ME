import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class DriverMIDlet extends MIDlet implements CommandListener {
  private DCanvas canvas;
  
  public void startApp() {
    if (canvas == null) {
      canvas = new DCanvas(Display.getDisplay(this));
      Command exitCommand = new Command("Выход", Command.EXIT, 0);
      canvas.addCommand(exitCommand);
      canvas.setCommandListener(this);
    }
    
    // Start up the canvas
    canvas.start();
  }
  
  public void pauseApp() {}
  
  public void destroyApp(boolean unconditional) {
    canvas.stop();
  }

  public void commandAction(Command c, Displayable s) {
    if (c.getCommandType() == Command.EXIT) {
      destroyApp(true);
      notifyDestroyed();
    }
  }
}
