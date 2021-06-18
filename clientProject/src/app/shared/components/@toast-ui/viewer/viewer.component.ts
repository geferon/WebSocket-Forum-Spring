import { Component, ElementRef, Input, OnChanges, OnDestroy, OnInit, SimpleChanges } from '@angular/core';
import { EditorCore, EventMap, ViewerOptions } from '@toast-ui/editor';
import { EditorPlugin, ExtendedAutolinks, LinkAttributes, Sanitizer, Viewer } from '@toast-ui/editor/types/editor';
import { HTMLConvertorMap } from '@toast-ui/toastmark';

interface ViewerInitOptions extends ViewerOptions {
	viewer: boolean;
}

@Component({
	selector: 'toast-ui-viewer',
	template: '',
	styleUrls: ['./viewer.component.scss'],
	host: {'class': 'toastui-editor-angular'}
})
export class ToastUiViewerComponent implements OnInit, OnDestroy, OnChanges {
	@Input() public markdown: string;
	@Input() public events: EventMap;
	@Input() public plugins: EditorPlugin[];
	@Input() public extendedAutolinks: ExtendedAutolinks;
	@Input() public linkAttributes: LinkAttributes;
	@Input() public customHTMLRenderer: HTMLConvertorMap;
	@Input() public referenceDefinition: boolean;
	@Input() public customHTMLSanitizer: Sanitizer;
	@Input() public frontMatter: boolean;
	@Input() public usageStatistics: boolean;

	private toastViewer: Viewer;

	constructor(private ref: ElementRef) { }

	ngOnInit(): void {
		let config: ViewerInitOptions = {
			el: this.ref.nativeElement,
			viewer: true
		};

		if (this.markdown) config.initialValue = this.markdown;
		if (this.events) config.events = this.events;
		if (this.plugins) config.plugins = this.plugins;
		if (this.extendedAutolinks) config.extendedAutolinks = this.extendedAutolinks;
		if (this.linkAttributes) config.linkAttributes = this.linkAttributes;
		if (this.customHTMLRenderer) config.customHTMLRenderer = this.customHTMLRenderer;
		if (this.referenceDefinition) config.referenceDefinition = this.referenceDefinition;
		if (this.customHTMLSanitizer) config.customHTMLSanitizer = this.customHTMLSanitizer;
		if (this.frontMatter) config.frontMatter = this.frontMatter;
		if (this.usageStatistics) config.usageStatistics = this.usageStatistics;

		this.toastViewer = EditorCore.factory(config);
	}

	ngOnDestroy(): void {
		this.toastViewer.destroy();
	}

	ngOnChanges(changes: SimpleChanges): void {
		if (!this.toastViewer) return;

		const propertiesToCheck = [
			'markdown',
			'events',
			'plugins',
			'extendedAutolinks',
			'linkAttributes',
			'customHTMLRenderer',
			'referenceDefinition',
			'customHTMLSanitizer',
			'frontMatter',
			'usageStatistics'
		];

		for (const property of propertiesToCheck) {
			if (changes[property] && changes[property].previousValue != changes[property].currentValue) {
				let setProperty = this.toastViewer['set' + property.charAt(0).toUpperCase() + property.slice(1)];
				if (setProperty) {
					setProperty.call(this.toastViewer, changes[property].currentValue);
				}
			}
		}
	}

}
