import { AfterViewInit, Component, Inject, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, FormGroupDirective, NgForm, Validators } from '@angular/forms';
import { ErrorStateMatcher } from '@angular/material/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatTabChangeEvent, MatTabGroup } from '@angular/material/tabs';
import { AuthService } from 'src/app/shared/services/auth.service';

class SamePasswordMatcher implements ErrorStateMatcher {
	isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
		const invalidCtrl = !!(control?.invalid && control?.dirty);
		const invalidParent = !!(control?.parent?.invalid && control?.dirty);

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
	selector: 'app-login-dialog',
	templateUrl: './login-dialog.component.html',
	styleUrls: ['./login-dialog.component.scss']
})
export class LoginDialogComponent implements OnInit, AfterViewInit {

	@ViewChild(MatTabGroup) private tabGroup: MatTabGroup;
	private activeTab = 0;

	// Formularios
	public loginForm = new FormGroup({
		username: new FormControl('', Validators.required),
		password: new FormControl('', Validators.required)
	});

	public registerForm = new FormGroup({
		username: new FormControl('', Validators.required),
		email: new FormControl('', [Validators.required, Validators.email]),
		password: new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(40)]),
		confirmPassword: new FormControl('', Validators.required)
	}, ConfirmValidator('password', 'confirmPassword'));

	public passwordMatcher = new SamePasswordMatcher();

	// Variables publicas
	public ocultarPass = true;

	public loginError: boolean | string = false;
	public registerError: boolean | string = false;


	constructor(
		@Inject(MAT_DIALOG_DATA) public data: any,
		private dialogRef: MatDialogRef<LoginDialogComponent>,
		private auth: AuthService
	) { }

	ngOnInit(): void {
		this.loginForm.valueChanges.subscribe(() => {
			this.loginError = false;
		});
		this.registerForm.valueChanges.subscribe(() => {
			this.registerError = false;
		});
	}

	ngAfterViewInit(): void {
		this.activeTab = this.tabGroup.selectedIndex;
	}

	checkPasswords(control: FormControl) {
		const password = control.value;
		const confirmPassword = this.registerForm.get('confirmPassword').value;

		return password === confirmPassword ? null : { notSame: true };
	}

	selectedTab($event: MatTabChangeEvent): void {
		// Reiniciar algunas variables
		this.ocultarPass = true;
		this.loginError = false;
		this.registerError = false;

		// Establecer pestaña activa
		this.activeTab = $event.index;
	}

	isValid(): boolean {
		switch (this.activeTab) {
			case 0:
				return this.loginForm.valid;
			case 1:
				return this.registerForm.valid;
		}
		return true;
	}

	isString(value: any): boolean {
		return typeof value === 'string';
	}

	login(): void {
		this.auth.login(this.loginForm.value.username, this.loginForm.value.password)
			.subscribe((result) => {
				if (result) this.dialogRef.close();
				this.loginForm.setErrors({ login: !result });
				this.loginError = !result;
			});
	}

	register(): void {
		this.auth.register(this.registerForm.value)
			.subscribe((result) => {
				this.dialogRef.close();
			}, (err) => {
				this.registerError = err?.error?.errors.map(e => e.field + " " + e.defaultMessage) ??
					err?.error?.message ?? err?.message ?? true;
			});
	}

}
