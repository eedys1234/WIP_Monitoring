package login;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import org.json.simple.JSONObject;

import alert.AlertDialog;
import api.RequestUtil;
import api.XNet;
import encoding.StringUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import rand.RandomUtil;
import secure.EncryptUtil;
import secure.Wipkey;
import secure.WipkeyHolder;

public class loginController implements Initializable {

	@FXML
	private Button btnLogin;

	@FXML
	private TextField txtAdmin;
	
	@FXML
	private TextField txtPW;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		JSONObject response = RequestUtil.getRequest(XNet.WIPKey, new JSONObject());
		if(!Objects.isNull(response)) {				
			WipkeyHolder holder = new WipkeyHolder(StringUtil.fixNull(response.get("WIPKey")));	
			holder.getWipkey();
		}		
	}
	
	public void login(ActionEvent event) {
		String admin = txtAdmin.getText();
		String pw = txtPW.getText();
		
		if("".equals(admin)) {
			AlertDialog.show(AlertType.ERROR, "아이디 미입력", "아이디를 입력해주세요.");
		}
		else if("".equals(pw)) {
			AlertDialog.show(AlertType.ERROR, "비밀번호 미입력", "비밀번호를 입력해주세요.");
		}
		else {
			JSONObject request = new JSONObject();
			String aesKey = RandomUtil.getRandomString(16);
			String wipKey = Wipkey.getKey();
						
			try {
			
				request.put("ID", EncryptUtil.encryptAES128(admin, aesKey));
				request.put("PW", EncryptUtil.encryptAES128(pw, aesKey));
				request.put("RandomKey", EncryptUtil.encryptStringRSA(aesKey, wipKey));
				request.put("WIPKey", wipKey);

				JSONObject response = RequestUtil.getRequest(XNet.LOGIN, request);
				String resValue = StringUtil.fixNull(response.get("resValue"));
				resValue = EncryptUtil.decryptAES128(resValue, aesKey);
				
				if("200".equals(resValue)) {
					
					Stage stage = (Stage)btnLogin.getScene().getWindow();
					Stage newStage = new Stage();

					Parent root = FXMLLoader.load(getClass().getResource("/view/serverinfo.fxml"));
					Scene scene = new Scene(root);
					scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
					
					stage.close();
					newStage.setScene(scene);
					newStage.show();
				}
				else {
					AlertDialog.show(AlertType.ERROR, "로그인 실패", "아이디 혹은 비밀번호가 틀렸습니다.");
				}				
	
			} catch (Exception e) {
				System.out.println();
			}
		}
		
	}
}
