import { UpdateCode } from './update-code';
import {async, TestBed} from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import createSpyObj = jasmine.createSpyObj;

describe('UpdateCode', () => {
  let httpClient: HttpClient;
  let httpTestingController: HttpTestingController;
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        UpdateCode,
        HttpTestingController
      ],
    }).compileComponents();
    httpClient = TestBed.inject(HttpClient);
    httpTestingController = TestBed.inject(HttpTestingController);
    }));

  it('spec name', () => {
    const data = {
      label: 'foo',
      children: []
    };
    // TODO
    // const cmd = createSpyObj<UpdateCode>();
    // cmd.doIt();
    const req = httpTestingController.expectOne('spoon/ast'); // on check la route
    req.flush(data); // on mock les données retournées

    expect(req.request.method).toEqual('POST'); // on check le verbe
  });

  afterEach(() => {
    // After every test, assert that there are no more pending requests.
    httpTestingController.verify();
  });
});
