import javax.swing.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class GuiImageTest extends JFrame implements MouseListener,MouseMotionListener {
	private JButton buttonArray[];//ボタン用の配列
	private Container c;
	private ImageIcon blackIcon, whiteIcon, boardIcon;

	public GuiImageTest() {
		//ウィンドウを作成する
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ウィンドウを閉じるときに，正しく閉じるように設定する
		setTitle("EventTest");		//ウィンドウのタイトルを設定する
		setSize(400,400);		//ウィンドウのサイズを設定する
		c = getContentPane();	//フレームのペインを取得する
		c.setLayout(null);		//自動レイアウトの設定を行わない

		//アイコンの設定
		whiteIcon = new ImageIcon("White.jpg");
		blackIcon = new ImageIcon("Black.jpg");
		boardIcon = new ImageIcon("GreenFrame.jpg");

		//ボタンの生成
		buttonArray = new JButton[5];//ボタンの配列を５個作成する[0]から[4]まで使える
		for(int i=0;i<5;i++){
			buttonArray[i] = new JButton(boardIcon);//ボタンにアイコンを設定する
			c.add(buttonArray[i]);//ペインに貼り付ける
			buttonArray[i].setBounds(i*45,10,45,45);//ボタンの大きさと位置を設定する．(x座標，y座標,xの幅,yの幅）
			buttonArray[i].addMouseListener(this);//ボタンをマウスでさわったときに反応するようにする
			buttonArray[i].addMouseMotionListener(this);//ボタンをマウスで動かそうとしたときに反応するようにする
			buttonArray[i].setActionCommand(Integer.toString(i));//ボタンに配列の情報を付加する（ネットワークを介してオブジェクトを識別するため）
		}
		
	}
		
	public static void main(String[] args) {
		GuiImageTest gui = new GuiImageTest();
		gui.setVisible(true);
	}
  	
	public void mouseClicked(MouseEvent e) {//ボタンをクリックしたときの処理
		System.out.println("クリック");
		JButton theButton = (JButton)e.getComponent();//クリックしたオブジェクトを得る．型が違うのでキャストする
		String theArrayIndex = theButton.getActionCommand();//ボタンの配列の番号を取り出す

		Icon theIcon = theButton.getIcon();//theIconには，現在のボタンに設定されたアイコンが入る
		System.out.println(theIcon);//デバッグ（確認用）に，クリックしたアイコンの名前を出力する

		if(theIcon.equals(boardIcon)){//アイコンがboardIconと同じなら
			theButton.setIcon(whiteIcon);//whiteIconに設定する
		}else if(theIcon.equals(whiteIcon)){
			theButton.setIcon(blackIcon);//blackIconに設定する
		}else{
			theButton.setIcon(boardIcon);
		}
		repaint();//画面のオブジェクトを描画し直す
	}
	
	public void mouseEntered(MouseEvent e) {//マウスがオブジェクトに入ったときの処理
	}
	
	public void mouseExited(MouseEvent e) {//マウスがオブジェクトから出たときの処理
	}
	
	public void mousePressed(MouseEvent e) {//マウスでオブジェクトを押したときの処理（クリックとの違いに注意）
	}
	
	public void mouseReleased(MouseEvent e) {//マウスで押していたオブジェクトを離したときの処理
	}
	
	public void mouseDragged(MouseEvent e) {//マウスでオブジェクトとをドラッグしているときの処理
	}

	public void mouseMoved(MouseEvent e) {//マウスがオブジェクト上で移動したときの処理
	}
}
