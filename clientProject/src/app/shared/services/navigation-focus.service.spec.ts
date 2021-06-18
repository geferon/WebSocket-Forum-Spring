import { TestBed } from '@angular/core/testing';

import { NavigationFocusService } from './navigation-focus.service';

describe('NavigationFocusService', () => {
  let service: NavigationFocusService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NavigationFocusService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
