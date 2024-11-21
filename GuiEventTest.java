import javax.swing.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class GuiEventTest extends JFrame implements MouseListener,MouseMotionListener {
	private JButton buttonArray[];//ボタン用の配列
	private Container c;

	public GuiEventTest() {
		//ウィンドウを作成する
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ウィンドウを閉じるときに，正しく閉じるように設定する
		setTitle("EventTest");		//ウィンドウのタイトルを設定する
		setSize(400,400);		//ウィンドウのサイズを設定する
		c = getContentPane();	//フレームのペインを取得する
		c.setLayout(null);		//自動レイアウトの設定を行わない

		//ボタンの生成
		buttonArray = new JButton[5];//ボタンの配列を５個作成する[0]から[4]まで使える
		for(int i=0;i<5;i++){
			buttonArray[i] = new JButton(Integer.toString(i));//ボタンにアイコンを設定する
			c.add(buttonArray[i]);//ペインに貼り付ける
			buttonArray[i].setBounds(i*50,10,50,50);//ボタンの大きさと位置を設定する．(x座標，y座標,xの幅,yの幅）
			buttonArray[i].addMouseListener(this);//ボタンをマウスでさわったときに反応するようにする
			buttonArray[i].addMouseMotionListener(this);//ボタンをマウスで動かそうとしたときに反応するようにする
			buttonArray[i].setActionCommand(Integer.toString(i));//ボタンに配列の情報を付加する（ネットワークを介してオブジェクトを識別するため）
		}
		
	}
		
	public static void main(String[] args) {
		GuiEventTest gui = new GuiEventTest();
		gui.setVisible(true);
	}
  	
	public void mouseClicked(MouseEvent e) {//ボタンをクリックしたときの処理
		System.out.println("クリック");
		JButton theButton = (JButton)e.getComponent();//クリックしたオブジェクトを得る．型が違うのでキャストする
		String theArrayIndex = theButton.getActionCommand();//ボタンの配列の番号を取り出す
		if (theButton.getText().substring(0,1).equals("*")) {
			theButton.setText(theArrayIndex);
		}
		else {
			theButton.setText("*"+theArrayIndex);	// クリックされるとボタン文字列の先頭に'*'を付ける
		}
	}
	
	public void mouseEntered(MouseEvent e) {//マウスがオブジェクトに入ったときの処理
		System.out.println("マウスが入った");
	}
	
	public void mouseExited(MouseEvent e) {//マウスがオブジェクトから出たときの処理
		System.out.println("マウス脱出");
	}
	
	public void mousePressed(MouseEvent e) {//マウスでオブジェクトを押したときの処理（クリックとの違いに注意）
		System.out.println("マウスを押した");
	}
	
	public void mouseReleased(MouseEvent e) {//マウスで押していたオブジェクトを離したときの処理
		System.out.println("マウスを放した");
	}
	
	public void mouseDragged(MouseEvent e) {//マウスでオブジェクトとをドラッグしているときの処理
		System.out.println("マウスをドラッグ");
		JButton theButton = (JButton)e.getComponent();//型が違うのでキャストする
		String theArrayIndex = theButton.getActionCommand();//ボタンの配列の番号を取り出す

		Point theMLoc = e.getPoint();//発生元コンポーネントを基準とする相対座標
		System.out.println(theMLoc);//デバッグ（確認用）に，取得したマウスの位置をコンソールに出力する
		Point theBtnLocation = theButton.getLocation();//クリックしたボタンを座標を取得する
		theBtnLocation.x += theMLoc.x-15;//ボタンの真ん中当たりにマウスカーソルがくるように補正する
		theBtnLocation.y += theMLoc.y-15;//ボタンの真ん中当たりにマウスカーソルがくるように補正する
		theButton.setLocation(theBtnLocation);//マウスの位置にあわせてオブジェクトを移動する
 
		repaint();//オブジェクトの再描画を行う
	}

	public void mouseMoved(MouseEvent e) {//マウスがオブジェクト上で移動したときの処理
		System.out.println("マウス移動");
		int theMLocX = e.getX();//マウスのx座標を得る
		int theMLocY = e.getY();//マウスのy座標を得る
		System.out.println(theMLocX+","+theMLocY);//コンソールに出力する
	}
}
