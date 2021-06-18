import { FocusMonitor } from '@angular/cdk/a11y';
import { Directionality } from '@angular/cdk/bidi';
import { FlexibleConnectedPositionStrategy, Overlay, OverlayConfig, OverlayRef, ScrollStrategy } from '@angular/cdk/overlay';
import {
	AfterContentInit,
	ComponentFactoryResolver,
	Directive,
	ElementRef,
	EventEmitter,
	Inject,
	Injector,
	Input,
	OnChanges,
	OnDestroy,
	OnInit,
	Optional,
	Output,
	ReflectiveInjector,
	Self,
	ViewContainerRef
} from '@angular/core';
import { MatMenuItem, MatMenuPanel, MatMenuTrigger, MAT_MENU_PANEL, MAT_MENU_SCROLL_STRATEGY, MenuPositionX, MenuPositionY } from '@angular/material/menu';

import {IconPickerComponent} from './icon-picker.component';

@Directive({
	// eslint-disable-next-line @angular-eslint/directive-selector
	selector: '[iconPicker]',
	exportAs: 'iconPicker',
	// eslint-disable-next-line @angular-eslint/no-host-metadata-property
})
export class IconPickerDirective extends MatMenuTrigger implements OnInit, AfterContentInit, OnDestroy {
	@Input() iconPicker: string;
	@Input() ipPlaceHolder = 'Search icon...';
	@Input() ipFallbackIcon = 'fas fa-user';
	@Input() ipIconSize = '16px';
	@Input() ipIconVerticalPadding = '6px'; // Top / Bottom
	@Input() ipIconHorizontalPadding = '10px'; // Left / Right
	@Input() ipIconPack = ['bs', 'fa5'];
	@Input() ipKeepSearchFilter = 'false';

	@Output() iconPickerSelect = new EventEmitter<string>(true);

	private dialog: IconPickerComponent;


	constructor(
		private cfr: ComponentFactoryResolver,
		_overlay: Overlay,
		_element: ElementRef<HTMLElement>,
		private vcr: ViewContainerRef,
		@Inject(MAT_MENU_SCROLL_STRATEGY) scrollStrategy: any,
		@Inject(MAT_MENU_PANEL) @Optional() parentMenu: MatMenuPanel,
		// `MatMenuTrigger` is commonly used in combination with a `MatMenuItem`.
		// tslint:disable-next-line: lightweight-tokens
		@Optional() @Self() _menuItemInstance: MatMenuItem,
		@Optional() _dir: Directionality,
		_focusMonitor?: FocusMonitor) {
		super(_overlay,
			_element,
			vcr,
			scrollStrategy,
			parentMenu,
			_menuItemInstance,
			_dir,
			_focusMonitor);
	}

	ngOnInit() {
		this.iconPicker = this.iconPicker || this.ipFallbackIcon || 'fa fa-user-plus';
		this.iconPickerSelect.emit(this.iconPicker);

		this._createMenu();
	}

	ngAfterContentInit() {
		this.menu = this.dialog.menu;
		super.ngAfterContentInit();
	}

	ngOnDestroy() {
		super.ngOnDestroy();
	}

	iconSelected(icon: string) {
		this.iconPickerSelect.emit(icon);
	}

	private _createMenu() {
		const vcRef = this.vcr;
		const compFactory = this.cfr.resolveComponentFactory(IconPickerComponent);
		// eslint-disable-next-line import/no-deprecated
		// const injector = ReflectiveInjector.fromResolvedProviders([], vcRef.parentInjector);
		const cmpRef = vcRef.createComponent(compFactory);
		// const cmpRef = vcRef.createComponent(compFactory, 0, injector, []);
		cmpRef.instance.setDialog(this, this.iconPicker,
			this.ipPlaceHolder, this.ipFallbackIcon, this.ipIconPack, this.ipIconSize,
			this.ipIconVerticalPadding, this.ipIconHorizontalPadding, this.ipKeepSearchFilter);
		this.dialog = cmpRef.instance;

		if (this.vcr !== vcRef) {
			cmpRef.changeDetectorRef.detectChanges();
		}
	}

}
