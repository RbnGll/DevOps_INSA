import { UpdateCode } from './update-code';
import {async, TestBed} from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient} from '@angular/common/http';
import {MatTreeNestedDataSource} from '@angular/material/tree';
import {ASTNode} from '../component/ast/ast.component';
import {CommandImpl} from 'interacto';

describe('UpdateCode', () => {
  let httpClient: HttpClient;
  let httpTestingController: HttpTestingController;
  let dataSource: MatTreeNestedDataSource<ASTNode>;
  let update: UpdateCode;
  const testData: ASTNode[] = [
    {
      label: 'Fruit',
      children: [
        {label: 'Apple'},
        {label: 'Banana'},
        {label: 'Fruit loops'},
      ]
    }, {
      label: 'Vegetables',
      children: [
        {
          label: 'Green',
          children: [
            {label: 'Broccoli'},
            {label: 'Brussels sprouts'},
          ]
        }, {
          label: 'Orange',
          children: [
            {label: 'Pumpkins'},
            {label: 'Carrots'},
          ]
        },
      ]
    },
  ];
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    httpClient = TestBed.inject(HttpClient);
    httpTestingController = TestBed.inject(HttpTestingController);
    dataSource = new MatTreeNestedDataSource();
    dataSource.data = testData;
    });

  it('should create UpdateCode instance of CommandImpl', () => {
    update = new UpdateCode(httpClient, dataSource);
    expect(update).toBeInstanceOf(CommandImpl);
  });

  it('spec REST tests', () => {
    update = new UpdateCode(httpClient, dataSource);
    const data = {
      label: 'foo',
      children: []
    };
    update.doIt();
    const req = httpTestingController.expectOne('spoon/ast'); // on check la route
    req.flush(data); // on mock les données retournées

    expect(req.request.method).toEqual('POST'); // on check le verbe
  });

  afterEach(() => {
    // After every test, assert that there are no more pending requests.
    httpTestingController.verify();
  });
});
