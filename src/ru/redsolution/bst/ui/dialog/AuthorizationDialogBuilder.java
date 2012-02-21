package ru.redsolution.bst.ui.dialog;

import ru.redsolution.bst.R;
import ru.redsolution.bst.data.BST;
import ru.redsolution.dialogs.AcceptAndDeclineDialogListener;
import ru.redsolution.dialogs.ConfirmDialogBuilder;
import ru.redsolution.dialogs.DialogBuilder;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Диалог авторизации.
 * 
 * Диалог не может быть закрыт кнопкой назад.
 * 
 * Автоматически сохраняет введенные имя пользователя и пароль.
 * 
 * @author alexander.ivanov
 * 
 */
public class AuthorizationDialogBuilder extends ConfirmDialogBuilder {

	private final EditText loginView;
	private final EditText passwordView;

	public AuthorizationDialogBuilder(final Activity activity, int dialogId,
			final AcceptAndDeclineDialogListener listener) {
		super(activity, dialogId, new AcceptAndDeclineDialogListener() {

			@Override
			public void onAccept(DialogBuilder dialogBuilder) {
				String login = ((AuthorizationDialogBuilder) dialogBuilder)
						.getLogin();
				if (!login.matches("^[\\u0000-\\u007F]*$")) {
					Toast.makeText(activity,
							activity.getString(R.string.incorrect_login),
							Toast.LENGTH_LONG).show();
					listener.onDecline(dialogBuilder);
				} else {
					BST.getInstance().setLoginAndPassword(
							login,
							((AuthorizationDialogBuilder) dialogBuilder)
									.getPassword());
					listener.onAccept(dialogBuilder);
				}
			}

			@Override
			public void onDecline(DialogBuilder dialogBuilder) {
				listener.onDecline(dialogBuilder);
			}

		});
		View dialogLayout = activity.getLayoutInflater().inflate(R.layout.auth,
				null, false);
		loginView = (EditText) dialogLayout.findViewById(R.id.login);
		passwordView = (EditText) dialogLayout.findViewById(R.id.password);
		loginView.setText(BST.getInstance().getLogin());
		passwordView.setText(BST.getInstance().getPassword());
		setView(dialogLayout);
		setTitle(R.string.auth_title);
	}

	public String getLogin() {
		return loginView.getText().toString();
	}

	public String getPassword() {
		return passwordView.getText().toString();
	}

}
