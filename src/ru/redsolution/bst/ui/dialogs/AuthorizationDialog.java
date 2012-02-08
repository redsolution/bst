package ru.redsolution.bst.ui.dialogs;

import ru.redsolution.bst.R;
import ru.redsolution.bst.data.BST;
import ru.redsolution.dialogs.ConfirmDialogBuilder;
import ru.redsolution.dialogs.DialogBuilder;
import ru.redsolution.dialogs.DialogListener;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;

/**
 * Диалог авторизации.
 * 
 * Автоматически сохраняет введенные имя пользователя и пароль.
 * 
 * @author alexander.ivanov
 * 
 */
public class AuthorizationDialog extends ConfirmDialogBuilder {

	private final EditText loginView;
	private final EditText passwordView;

	public AuthorizationDialog(Activity activity, int dialogId,
			final DialogListener listener) {
		super(activity, dialogId, new DialogListener() {

			@Override
			public void onAccept(DialogBuilder dialogBuilder) {
				BST.getInstance().setLoginAndPassword(
						((AuthorizationDialog) dialogBuilder).getLogin(),
						((AuthorizationDialog) dialogBuilder).getPassword());
				listener.onAccept(dialogBuilder);
			}

			@Override
			public void onDecline(DialogBuilder dialogBuilder) {
				listener.onDecline(dialogBuilder);
			}

			@Override
			public void onCancel(DialogBuilder dialogBuilder) {
				listener.onCancel(dialogBuilder);
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
