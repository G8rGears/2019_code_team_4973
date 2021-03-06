/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
//import edu.wpi.first.wpilibj.buttons.Button;
//import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
//import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMVictorSPX;
//import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Servo;

/**
* The VM is configured to automatically run this class, and to call the
* functions corresponding to each mode, as described in the TimedRobot
* documentation. If you change the name of this class or the package after
* creating this project, you must also update the build.gradle file in the
* project.
*/
public class Robot extends TimedRobot {
 private static final String kDefaultAuto = "Default";
 private static final String kCustomAuto = "My Auto";
 private String m_autoSelected;
 private final SendableChooser<String> m_chooser = new SendableChooser<>();
 private final DifferentialDrive m_robotDrive = new DifferentialDrive(new PWMVictorSPX(0),new PWMVictorSPX(1));
 private final XboxController m_stick = new XboxController(0);
// PWMVictorSPX m_sol = new PWMVictorSPX(2);
// Spark m_sol2 = new Spark(4);
 PWMVictorSPX m_wind = new PWMVictorSPX(4);
 private final Timer m_timer = new Timer();
 public int failsafe = 1;
 Servo servo = new Servo(3);
 PWMVictorSPX m_sol1 = new PWMVictorSPX();
 PWMVictorSPX m_sol1 = new PWMVictorSPX();  

 /**
  * This function is run when the robot is first started up and should be
  * used for any initialization code.
  */

 Thread m_visionThread;

 @Override
 public void robotInit() {
   m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
   m_chooser.addOption("My Auto", kCustomAuto);
   SmartDashboard.putData("Auto choices", m_chooser);
   // get the usb camera from CameraServer
   UsbCamera camera = CameraServer.getInstance().startAutomaticCapture(0);
   // set the resolution
   camera.setResolution(320, 240);
 }


 /**
  * This function is called every robot packet, no matter the mode. Use
  * this for items like diagnostics that you want ran during disabled,
  * autonomous, teleoperated and test.
  *
  * <p>This runs after the mode specific periodic functions, but before
  * LiveWindow and SmartDashboard integrated updating.
  */
 @Override
 public void robotPeriodic() {
 }

 /**
  * This autonomous (along with the chooser code above) shows how to select
  * between different autonomous modes using the dashboard. The sendable
  * chooser code works with the Java SmartDashboard. If you prefer the
  * LabVIEW Dashboard, remove all of the chooser code and uncomment the
  * getString line to get the auto name from the text box below the Gyro
  *
  * <p>You can add additional auto modes by adding additional comparisons to
  * the switch structure below with additional strings. If using the
  * SendableChooser make sure to add them to the chooser code above as well.
  */
 @Override
 public void autonomousInit() {
   m_autoSelected = m_chooser.getSelected();
   // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
   System.out.println("Auto selected: " + m_autoSelected);
 }

 /**
  * This function is called periodically during autonomous.
 */
 @Override
 public void autonomousPeriodic() {
  /** switch (m_autoSelected) {
     case kCustomAuto:
       // Put custom auto code here
       break;
     case kDefaultAuto:
     default:
       // Put default auto code here
       break;
   }
   */

   //m_robotDrive.arcadeDrive(m_stick.getY(), m_stick.getX());
   teleopPeriodic();

 }
 @Override
 public void teleopInit() {
   //m_sol.set(0);
 }
 /**
  * This function is called periodically during operator control.
  */


 @Override
 public void teleopPeriodic() {
   m_robotDrive.arcadeDrive(m_stick.getY(), m_stick.getX());
   boolean sButtonVaule;
   boolean rButtonValue;
   sButtonVaule = m_stick.getRawButton(11);
    if (sButtonVaule) {
      m_timer.reset();
      m_timer.start();
      boolean b = m_timer.get() <= 2.0;
      while (b){
        m_sol.set(1);
        b = m_timer.get() <= 2.0;
      }
      m_sol.set(0);
      }
   /*
   sButtonVaule = m_stick.getBumperPressed(Hand.kLeft);
   if (sButtonVaule) {
    // m_timer.reset();
    //m_timer.start();
    // boolean b = m_timer.get() <= 3.0;
     //while (b){
      // servo.set(0.82);
       //b = m_timer.get() <= 3.0;

       servo.set(0.5);
     }
     {
      
       rButtonValue = m_stick.getBumperPressed(Hand.kRight);
       if (rButtonValue) {
         servo.set(0.18);
       }
     }
    // servo.set(0.18);
    */


     boolean bkButtonVaule; 
     bkButtonVaule = m_stick.getYButton();;
     if (bkButtonVaule) {
       m_wind.set(0);
     }

     boolean efButtonVaule;
     efButtonVaule = m_stick.getStartButton();
     if (efButtonVaule) {
       m_wind.set(.33);
     }

     boolean ebButtonVaule;
     ebButtonVaule = m_stick.getBackButton();
     if (ebButtonVaule) {
       m_wind.set(-.33);
     }

     double windrotation = 0.7;
     boolean bwButtonVaule;
     bwButtonVaule = m_stick.getXButton();;
     if (bwButtonVaule) {
       if (failsafe == 0) {
         m_timer.reset();
         m_timer.start();
         boolean wftime = m_timer.get()  < windrotation;
         while (wftime){
         m_wind.set(-1);
           if (bkButtonVaule) {
           m_wind.set(0);
           }     
         wftime = m_timer.get()  < windrotation;
         }
         m_wind.set(0);
         failsafe = 1;
       }
     }

     boolean fwButtonVaule;
     fwButtonVaule = m_stick.getBButton();;
     if (fwButtonVaule==true) {
       if (failsafe == 1) {
         m_timer.reset();
         m_timer.start();
         boolean wftime = m_timer.get()  < windrotation;
         while (wftime){
         m_wind.set(1);
         if (bkButtonVaule) {
           m_wind.set(0);
         }
         wftime = m_timer.get()  < windrotation;
         }
         m_wind.set(0);
         failsafe = 0;
       }
     
     }
  
     SmartDashboard.putNumber("window motor", m_wind.get());

}

 /**
  * This function is called periodically during test mode.
  */
 @Override
 public void testPeriodic() {
 }
}

