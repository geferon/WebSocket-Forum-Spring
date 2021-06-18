import { Component, OnInit, ChangeDetectionStrategy, ElementRef, Input, OnDestroy, OnChanges, SimpleChanges, Output, EventEmitter, forwardRef, Inject, LOCALE_ID } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

import { Editor, ToMdConvertorMap, PluginInfo } from '@toast-ui/editor';
import { EditorOptions, EditorType, EventMap, ExtendedAutolinks, HookMap, LinkAttributes, PluginContext, PreviewStyle, Sanitizer, WidgetRule } from '@toast-ui/editor/types/editor';
import { ToolbarItemOptions } from '@toast-ui/editor/types/ui';
import { HTMLConvertorMap } from '@toast-ui/toastmark';
type PluginFn = (context: PluginContext, options?: Record<string, any>) => PluginInfo | null;
type EditorPlugin = PluginFn | [PluginFn, Record<string, any>];

// Languages
import '@toast-ui/editor/dist/i18n/es-es';


@Component({
	selector: 'toast-ui-editor',
	template: '',
	styleUrls: ['./editor.component.scss'],
	providers: [
		{
			provide: NG_VALUE_ACCESSOR,
			useExisting: forwardRef(() => ToastUiEditorComponent),
			multi: true
		}
	]
})
export class ToastUiEditorComponent implements OnInit, OnDestroy, OnChanges, ControlValueAccessor {

	@Input() public height: string;
	@Input() public minHeight: string;
	@Input() public previewStyle: PreviewStyle;
	@Input() public initialEditType: EditorType;
	@Input() public events: EventMap;
	@Input() public hooks: HookMap;
	@Input() public language: string = this.locale;
	@Input() public useCommandShortcut: boolean;
	@Input() public usageStatistics: boolean;
	@Input() public toolbarItems: (string | ToolbarItemOptions)[];
	@Input() public hideModeSwitch: boolean;
	@Input() public plugins: EditorPlugin[];
	@Input() public extendedAutolinks: ExtendedAutolinks;
	@Input() public placeholder: string;
	@Input() public linkAttributes: LinkAttributes;
	@Input() public customHTMLRenderer: HTMLConvertorMap;
	@Input() public customMarkdownRenderer: ToMdConvertorMap;
	@Input() public referenceDefinition: boolean;
	@Input() public customHTMLSanitizer: Sanitizer;
	@Input() public previewHighlight: boolean;
	@Input() public frontMatter: boolean;
	@Input() public widgetRules: WidgetRule[];
	@Input() public theme: string = 'angular';

	@Output() public load = new EventEmitter<void>();
	@Output() public focus = new EventEmitter<void>();
	@Output() public blur = new EventEmitter<void>();

	private value = "";
	private toastEditor: Editor;
	private onChange = (_) => { };
	private onTouched = () => { };

	constructor(
		private ref: ElementRef,
		@Inject(LOCALE_ID) private locale: string
	) { }

	writeValue(markdown: string): void {
		this.value = markdown;
		this.toastEditor?.setMarkdown(markdown ?? '');
	}
	registerOnChange(fn: (_: any) => void): void { this.onChange = fn; }
	registerOnTouched(fn: () => void): void { this.onTouched = fn; }

	ngOnInit(): void {
		let config: EditorOptions = {
			el: this.ref.nativeElement,
			initialValue: this.value ?? ""
		};
		if (this.height) config.height = this.height;
		if (this.minHeight) config.minHeight = this.minHeight;
		if (this.previewStyle) config.previewStyle = this.previewStyle;
		if (this.initialEditType) config.initialEditType = this.initialEditType;
		if (this.events) config.events = this.events;
		if (this.hooks) config.hooks = this.hooks;
		if (this.language) config.language = this.language;
		if (this.useCommandShortcut) config.useCommandShortcut = this.useCommandShortcut;
		if (this.usageStatistics) config.usageStatistics = this.usageStatistics;
		if (this.toolbarItems) config.toolbarItems = this.toolbarItems;
		if (this.hideModeSwitch) config.hideModeSwitch = this.hideModeSwitch;
		if (this.plugins) config.plugins = this.plugins;
		if (this.extendedAutolinks) config.extendedAutolinks = this.extendedAutolinks;
		if (this.placeholder) config.placeholder = this.placeholder;
		if (this.linkAttributes) config.linkAttributes = this.linkAttributes;
		if (this.customHTMLRenderer) config.customHTMLRenderer = this.customHTMLRenderer;
		if (this.customMarkdownRenderer) config.customMarkdownRenderer = this.customMarkdownRenderer;
		if (this.referenceDefinition) config.referenceDefinition = this.referenceDefinition;
		if (this.customHTMLSanitizer) config.customHTMLSanitizer = this.customHTMLSanitizer;
		if (this.previewHighlight) config.previewHighlight = this.previewHighlight;
		if (this.frontMatter) config.frontMatter = this.frontMatter;
		if (this.widgetRules) config.widgetRules = this.widgetRules;
		if (this.theme) config.theme = this.theme;

		this.toastEditor = new Editor(config);

		this.toastEditor.on('load', () => this.load.emit());
		this.toastEditor.on('focus', () => {
			this.focus.emit();
			this.onTouched();
		});
		this.toastEditor.on('blur', () => this.blur.emit());
		this.toastEditor.on('change', () => this.onChange(this.value = this.toastEditor.getMarkdown()));
	}

	ngOnDestroy(): void {
		this.toastEditor.destroy();
	}

	ngOnChanges(changes: SimpleChanges): void {
		if (!this.toastEditor) return;

		const propertiesToCheck = [
			'height',
			'minHeight',
			'initialValue',
			'previewStyle',
			'initialEditType',
			'events',
			'hooks',
			'language',
			'useCommandShortcut',
			'usageStatistics',
			'toolbarItems',
			'hideModeSwitch',
			'plugins',
			'extendedAutolinks',
			'placeholder',
			'linkAttributes',
			'customHTMLRenderer',
			'customMarkdownRenderer',
			'referenceDefinition',
			'customHTMLSanitizer',
			'previewHighlight',
			'frontMatter',
			'widgetRules',
			'theme'
		];

		for (const property of propertiesToCheck) {
			if (changes[property] && changes[property].previousValue != changes[property].currentValue) {
				let setProperty = this.toastEditor['set' + property.charAt(0).toUpperCase() + property.slice(1)];
				if (setProperty) {
					setProperty.call(this.toastEditor, changes[property].currentValue);
				}
			}
		}
	}

}
