import {ChangeDetectorRef, Component, ElementRef, OnInit, ViewChild} from '@angular/core';

import {IconPickerService} from './icon-picker.service';
import {Icon, IconType} from './icon';
import { MatMenu } from '@angular/material/menu';

@Component({
	// eslint-disable-next-line @angular-eslint/component-selector
	selector: 'icon-picker',
	templateUrl: './icon-picker.component.html',
	styleUrls: ['./icon-picker.component.scss']
})

export class IconPickerComponent implements OnInit {
	@ViewChild('dialogPopup') menu: MatMenu;

	// Icon css
	public ipIconSize: number;
	public ipIconVerticalPadding: number;
	public ipIconHorizontalPadding: number;
	// Icon and behaviors
	public ipKeepSearchFilter: boolean;
	public ipPlaceHolder: string;
	public ipFallbackIcon: string;
	public ipIconPack: string[];

	public top: number;
	public left: number;
	public position: string;
	public arrowTop: number;
	public selectedIcon: Icon;
	public iconType = IconType;
	public buttonWidth: number;
	public buttonHeight: number;

	icons: Icon[] = [];
	search = '';

	private directiveInstance: any;
	private initialIcon: string;

	constructor(
		private el: ElementRef,
		private cdr: ChangeDetectorRef,
		private service: IconPickerService) {
	}

	setDialog(instance: any, icon: string, ipPlaceHolder: string,
			ipFallbackIcon: string, ipIconPack: string[], ipIconSize: string,
			ipIconVerticalPadding: string, ipIconHorizontalPadding: string, ipKeepSearchFilter: string) {
		this.directiveInstance = instance;
		this.setInitialIcon(icon);
		this.ipIconSize = parseInt(ipIconSize, 10);
		this.ipIconVerticalPadding = parseInt(ipIconVerticalPadding, 10);
		this.ipIconHorizontalPadding = parseInt(ipIconHorizontalPadding, 10);
		this.ipKeepSearchFilter = JSON.parse(ipKeepSearchFilter);
		this.ipPlaceHolder = ipPlaceHolder;
		this.ipFallbackIcon = ipFallbackIcon;
		this.ipIconPack = ipIconPack;

		this.buttonHeight = this.ipIconSize + 2 * this.ipIconVerticalPadding;
		this.buttonWidth = this.ipIconSize + 2 * this.ipIconHorizontalPadding;
	}

	ngOnInit() {
		this.icons = this.service.getIcons(this.ipIconPack);
		this.setInitialIcon(this.initialIcon);
	}

	setInitialIcon(icon: string) {
		this.initialIcon = icon;
		this.selectedIcon = this.icons.find(el => el ?
			`fa fa-${el.id}` === icon || `glyphicon glyphicon-${el.id}` === icon || `${el.id}` === icon :
			false
		);
		if (this.ipKeepSearchFilter && this.selectedIcon && icon !== this.ipFallbackIcon) {
			this.search = this.selectedIcon.id;
		} else {
			this.search = '';
		}
	}

	setSearch(val: string) {
		this.search = val;
	}

	selectIcon(icon: Icon): void {
		if (icon.type === IconType.FontAwesome) {
			this.directiveInstance.iconSelected(`fa fa-${icon.id}`);
		} else if (icon.type === IconType.Bootstrap) {
			this.directiveInstance.iconSelected(`glyphicon glyphicon-${icon.id}`);
		} else if (icon.type === IconType.FontAwesome5) {
			this.directiveInstance.iconSelected(`${icon.id}`);
		} else if (icon.type === IconType.Material) {
			this.directiveInstance.iconSelected(`${icon.id}`);
		}
		this.menu.closed.emit();
	}
}
