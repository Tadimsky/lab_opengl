import java.awt.*;
import java.awt.event.KeyEvent;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import framework.JOGLFrame;
import framework.Scene;


/**
 * Display a simple scene to demonstrate OpenGL.
 *
 * @author Robert C. Duvall
 */
public class Robot extends Scene {

    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }
    
    // sections of the robot arm
    private Section[] mySections = {
        new Section(2.0f, Color.red),
        new Section(1.6f, Color.cyan),
        new Section(0.4f, Color.pink), 
        new Section(0.1f, Color.white)
    };


    /**
     * Create the scene with the given arguments. For example, the number of strands to display.
     *
     * @param args command-line arguments
     */
    public Robot (String[] args) {
        super("Robot Arm");
    }

    /**
     * Draw all of the objects to display.
     */
    @Override
    public void display (GL2 gl, GLU glu, GLUT glut) {
        // display each piece of the arm
        gl.glTranslatef(-1, 0, 0);
        for (Section s : mySections) {
            s.display(gl, glu, glut);
        }
    }

    /**
     * Set the camera's view of the scene.
     */
    @Override
    public void setCamera (GL2 gl, GLU glu, GLUT glut) {
        glu.gluLookAt(0, 2, 10, // from position
                      0, 0, 0,  // to position
                      0, 1, 0); // up direction
    }

    /**
     * Called when any key is pressed within the canvas. Turns each part of the arm separately.
     */
    @Override
    public void keyPressed (int keyCode) {
        switch (keyCode) {
          case KeyEvent.VK_1:
            mySections[0].turn(5);
            break;
          case KeyEvent.VK_A:
            mySections[0].turn(-5);
            break;
          case KeyEvent.VK_2:
            mySections[1].turn(5);
            break;
          case KeyEvent.VK_S:
            mySections[1].turn(-5);
            break;
          case KeyEvent.VK_3:
            mySections[2].turn(5);
            break;
          case KeyEvent.VK_D:
            mySections[2].turn(-5);
            break;
          case KeyEvent.VK_R:
            for (Section s : mySections) {
                s.reset();
            }
            break;
        }
    }

    /**
     * Separate class to represent a section of the arm. Holds the length of the piece and amount it
     * has been rotated.
     */
    static class Section {
        private float angle;
        private float length;
        private float[] myColor;

        public Section (float scale, Color c) {
            length = scale;
            myColor = c.getRGBComponents(null);
            reset();
        }

        public void turn (float degrees) {            
            angle += degrees;
            angle = clamp(angle, 0, 135);
        }

        public void reset () {
            angle = 0;
        }

        private void display (GL2 gl, GLU glu, GLUT glut) {            
            gl.glRotatef(angle,0,0,1);
            gl.glTranslatef(0, -0.5f, 0);
            gl.glTranslatef(length / 2, 0, 0);
            gl.glPushMatrix();
            {
                gl.glColor4fv(myColor, 0);
                //gl.glTranslatef(0, 0.5f, 0);
                gl.glScalef(length, 1, 1);
                glut.glutSolidCube(1);
            }
            gl.glPopMatrix();
            gl.glTranslatef(0, 0.5f, 0);
            gl.glTranslatef(length /2, 0,0);
        }
    }

    // allow program to be run from here
    public static void main (String[] args) {
        new JOGLFrame(new Robot(args));
    }
}
