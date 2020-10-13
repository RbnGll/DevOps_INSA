import { TestBed } from '@angular/core/testing';

import {NumberService, Random} from './number.service';

describe('NumberService', () => {
  let service: NumberService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NumberService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should be instantiated at zero', () => {
    expect(service.cpt).toBe(0);
  });

  it('after one increment equal to 1', () => {
    service.incrementCpt();
    expect(service.cpt).toBe(1);
  });

  it('after two increment equal to 2', () => {
    service.incrementCpt();
    service.incrementCpt();
    expect(service.cpt).toBe(2);
  });

  it('next Random should send random', () => {
    const random: Random = {
      nextNumber: () => 5,
      nextString: () => 'foo'
    };
    service.random = random;
    expect(service.nextRandom()).toBe(5);
  });
});
