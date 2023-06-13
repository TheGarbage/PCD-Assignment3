package resources

import actors.BootActor
import akka.actor.typed.ActorSystem

import java.awt.{BorderLayout, Color}
import java.awt.event.{ActionEvent, ActionListener, WindowEvent, WindowListener}
import javax.swing._
import javax.swing.border.EmptyBorder
import javax.swing.event.{ChangeEvent, ChangeListener, PopupMenuEvent, PopupMenuListener}

case class View(titleText: String) extends JFrame(titleText) with ActionListener with WindowListener with ChangeListener with PopupMenuListener {
  // Text utilities
  private val rankingTitle = "-------------------   RANKING -------------------"
  private val intervalsTitle = "----------   INTERVALS DIVISION   ----------"
  private val maxSizePath = 45

  private val n = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1))
  private val maxl = new JSpinner(new SpinnerNumberModel(2, 2, Integer.MAX_VALUE, 1))
  private val ni = new JComboBox[Int]()
  private val processState = new JTextArea()
  private val directorySelected = new JTextArea(" No directory selected")
  private val directoryButton = new JButton("   Select Direcotory   ")
  private val rankingText = new JTextArea(rankingTitle, 1, 22)
  private val rankingScrollPane = new JScrollPane(rankingText)
  private val intervalsText = new JTextArea(intervalsTitle, 1, 22)
  private val intervalsScrollPane = new JScrollPane(intervalsText)
  private val startButton = new JButton("   Start   ")
  private val stopButton = new JButton("   Stop   ")
  private val fileChooser = new JFileChooser("/")

  val system: ActorSystem[BootActor.Msg] = ActorSystem(BootActor(this), name = "Boot")

  initializeUI()

  private def initializeUI(): Unit = {
    setSize(600, 600)
    val parametersPanel = new JPanel()
    parametersPanel.add(new JLabel("N: "))
    parametersPanel.add(n)
    parametersPanel.add(new JLabel("          MAXL: "))
    parametersPanel.add(maxl)
    parametersPanel.add(new JLabel("          NI: "))
    ni.addItem(3)
    ni.setPrototypeDisplayValue(1111111)
    parametersPanel.add(ni)
    n.getEditor.asInstanceOf[JSpinner.DefaultEditor].getTextField.setColumns(5)
    maxl.getEditor.asInstanceOf[JSpinner.DefaultEditor].getTextField.setColumns(5)
    parametersPanel.setBorder(new EmptyBorder(10, 10, 10, 10))

    val selectorPanel = new JPanel()
    directorySelected.setEditable(false)
    directorySelected.setColumns(30)
    selectorPanel.add(directorySelected)
    selectorPanel.add(directoryButton)
    selectorPanel.setBorder(new EmptyBorder(10, 10, 15, 10))

    val northPanel = new JPanel()
    northPanel.setLayout(new BorderLayout())
    northPanel.add(BorderLayout.NORTH, selectorPanel)
    northPanel.add(BorderLayout.SOUTH, parametersPanel)

    val centerPanel = new JPanel()
    centerPanel.setLayout(new BorderLayout())
    rankingText.setEditable(false)
    rankingText.setBorder(new EmptyBorder(10, 10, 10, 10))
    intervalsText.setEditable(false)
    intervalsText.setBorder(new EmptyBorder(10, 10, 10, 10))
    intervalsScrollPane.setBorder(BorderFactory.createLineBorder(Color.black))
    intervalsScrollPane.setBorder(new EmptyBorder(10, 10, 10, 10))
    rankingScrollPane.setBorder(BorderFactory.createLineBorder(Color.black))
    rankingScrollPane.setBorder(new EmptyBorder(10, 10, 10, 10))
    centerPanel.add(BorderLayout.WEST, rankingScrollPane)
    centerPanel.add(BorderLayout.EAST, intervalsScrollPane)

    val infoPanel = new JPanel()
    processState.setText(" Parameter entry")
    processState.setEditable(false)
    processState.setColumns(20)
    infoPanel.add(new JLabel("State: "))
    infoPanel.add(processState)
    infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10))

    val buttonPanel = new JPanel()
    buttonPanel.setLayout(new BorderLayout())
    startButton.setEnabled(false)
    stopButton.setEnabled(false)
    buttonPanel.add(BorderLayout.WEST, startButton)
    buttonPanel.add(BorderLayout.EAST, stopButton)
    buttonPanel.setBorder(new EmptyBorder(10, 380, 10, 10))

    val southPanel = new JPanel()
    southPanel.setLayout(new BorderLayout())
    southPanel.add(BorderLayout.NORTH, infoPanel)
    southPanel.add(BorderLayout.SOUTH, buttonPanel)

    val mainPanel = new JPanel()
    mainPanel.setLayout(new BorderLayout())
    mainPanel.add(BorderLayout.NORTH, northPanel)
    mainPanel.add(BorderLayout.CENTER, centerPanel)
    mainPanel.add(BorderLayout.SOUTH, southPanel)
    mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10))

    setContentPane(mainPanel)

    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)
    addWindowListener(this)
    directoryButton.addActionListener(this)
    maxl.addChangeListener(this)
    ni.addPopupMenuListener(this)
    startButton.addActionListener(this)
    stopButton.addActionListener(this)

    setVisible(true)
  }

  override def actionPerformed(e: ActionEvent): Unit = {
    if (e.getSource == directoryButton) {
      if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        val path = fileChooser.getSelectedFile.getAbsolutePath
        val displayedPath = if (path.length > maxSizePath) " ..." + path.substring(path.length - maxSizePath) else path
        directorySelected.setText(displayedPath)
        startButton.setEnabled(true)
        processState.setText(" Waiting to start")
      }
    } else if (e.getSource == startButton) {
      rankingText.setText(rankingTitle + "\n\n No Java Files")
      intervalsText.setText(intervalsTitle + "\n\n No Java Files")
      sendMessage(BootActor.Command.Start)
      setIdle(false)
    } else if (e.getSource == stopButton) {
      sendMessage(BootActor.Command.Stop)
    }
  }

  override def windowOpened(e: WindowEvent): Unit = {}

  override def windowClosing(e: WindowEvent): Unit = {
    sendMessage(BootActor.Command.Stop)
    dispose()
    System.exit(0)
  }

  override def windowClosed(e: WindowEvent): Unit = {}

  override def windowIconified(e: WindowEvent): Unit = {}

  override def windowDeiconified(e: WindowEvent): Unit = {}

  override def windowActivated(e: WindowEvent): Unit = {}

  override def windowDeactivated(e: WindowEvent): Unit = {}

  override def stateChanged(e: ChangeEvent): Unit = {
    if (e.getSource == maxl) {
      val niValue = ni.getSelectedItem.asInstanceOf[Int]
      val maxlValue = maxl.getValue.asInstanceOf[Int]
      if (maxlValue < (niValue - 1) || maxlValue % (niValue - 1) != 0) {
        ni.removeAllItems()
        if (maxlValue == 2)
          ni.addItem(3)
        else
          ni.addItem(2)
      }
    }
  }

  private def setIdle(enable: Boolean): Unit = {
    n.setEnabled(enable)
    ni.setEnabled(enable)
    maxl.setEnabled(enable)
    directoryButton.setEnabled(enable)
    startButton.setEnabled(enable)
    stopButton.setEnabled(!enable)
  }

  def setFinish(text: String): Unit = {
    SwingUtilities.invokeLater(() => processState.setText(text))
    setIdle(true)
  }

  def setExecution(text: String): Unit = {
    SwingUtilities.invokeLater(() => processState.setText(text))
  }

  def setIntervalsText(text: String): Unit = {
    SwingUtilities.invokeLater(() => intervalsText.setText(text))
  }

  def setRankingText(text: String): Unit = {
    SwingUtilities.invokeLater(() => rankingText.setText(text))
  }

  override def popupMenuWillBecomeVisible(e: PopupMenuEvent): Unit = {
    if (e.getSource == ni) {
      maxl.commitEdit()
      val niValue: Int = ni.getSelectedItem.asInstanceOf[Int]
      ni.removeAllItems()
      val maxlValue: Int = maxl.getValue.asInstanceOf[Int]
      if (maxlValue != 2) {
        for (i <- 1 until maxlValue) {
          if (maxlValue % i == 0)
            ni.addItem(i + 1)
        }
        if (maxlValue < Integer.MAX_VALUE)
          ni.addItem(maxlValue + 1)
        ni.setSelectedItem(niValue)
      } else {
        ni.addItem(3)
      }
    }
  }

  override def popupMenuWillBecomeInvisible(e: PopupMenuEvent): Unit = {}

  override def popupMenuCanceled(e: PopupMenuEvent): Unit = {}

  def sendMessage(command: BootActor.Command.Action) : Unit =
    system ! BootActor.Msg( if (startButton.isEnabled) {
      fileChooser.getSelectedFile.getAbsolutePath
    } else {
      ""
    }, n.getValue.asInstanceOf[Int], maxl.getValue.asInstanceOf[Int], ni.getSelectedItem.asInstanceOf[Int], command)

}

object View {
  def apply (): View = View("Akka")
}