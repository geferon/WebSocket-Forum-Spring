import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormGroupDirective, NgForm, Validators } from '@angular/forms';
import { ErrorStateMatcher } from '@angular/material/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { take } from 'rxjs/operators';
import { AuthService } from 'src/app/shared/services/auth.service';

class SamePasswordMatcher implements ErrorStateMatcher {
	isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
		const invalidCtrl = !!(control?.invalid && control?.dirty);
		const invalidParent = !!(control?.parent?.invalid && control?.dirty && control?.hasError('mustMatch'));

		return invalidCtrl || invalidParent;
	}
}

function ConfirmValidator(controlName: string, matchingControlName: string) {
	return (formGroup: FormGroup) => {
		const control = formGroup.controls[controlName];
		const matchingControl = formGroup.controls[matchingControlName];
		if (matchingControl.errors && !matchingControl.errors.mustMatch) {
			return null;
		}
		if (control.value !== matchingControl.value) {
			return { mustMatch: true };
		}
		return null;
	}
}

@Component({
	selector: 'app-password-change',
	templateUrl: './password-change.component.html',
	styleUrls: ['./password-change.component.scss']
})
export class PasswordChangeDialogComponent implements OnInit {
	public passwordForm = new FormGroup({
		oldPassword: new FormControl('', [Validators.required]),
		newPassword: new FormControl('', [Validators.required]),
		newPasswordConfirm: new FormControl('', [Validators.required])
	}, ConfirmValidator('newPassword', 'newPasswordConfirm'));


	public passwordMatcher = new SamePasswordMatcher();


	constructor(
		@Inject(MAT_DIALOG_DATA) public data: any,
		private dialogRef: MatDialogRef<PasswordChangeDialogComponent>,
		private auth: AuthService
	) { }

	ngOnInit(): void {
	}

	public changePassword(): void {
		this.auth.changePassword(this.passwordForm.value.oldPassword, this.passwordForm.value.newPassword)
			.subscribe(result => {
				if (result) {
					this.dialogRef.close();
				} else {
					console.log(":(");
					this.passwordForm.controls.oldPassword.setErrors({invalidPass: true});
					this.passwordForm.controls.oldPassword.valueChanges.pipe(take(1)).subscribe(() => {
						this.passwordForm.controls.oldPassword.setErrors({});
					});
				}
			});
	}

}
