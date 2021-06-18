import { TestBed } from '@angular/core/testing';

import { LocationHistoryService } from './location-history.service';

describe('LocationHistoryService', () => {
  let service: LocationHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LocationHistoryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
