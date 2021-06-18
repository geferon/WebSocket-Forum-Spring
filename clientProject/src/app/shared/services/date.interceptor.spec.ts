import { TestBed } from '@angular/core/testing';

import { DateHttpInterceptor } from './date.interceptor';

describe('DateInterceptor', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
		  DateHttpInterceptor
      ]
  }));

  it('should be created', () => {
	  const interceptor: DateHttpInterceptor = TestBed.inject(DateHttpInterceptor);
    expect(interceptor).toBeTruthy();
  });
});
