import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {AstComponent, ASTNode} from './ast.component';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {NestedTreeControl} from '@angular/cdk/tree';
import {MatTreeNestedDataSource} from '@angular/material/tree';

describe('AstComponent', () => {
  let component: AstComponent;
  let fixture: ComponentFixture<AstComponent>;

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
      declarations: [ AstComponent ],
      imports: [ HttpClientTestingModule ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AstComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have treeControl', () => {
    expect(component.treeControl).toBeInstanceOf(NestedTreeControl);
  });

  it('should have a dataSource', () => {
    expect(component.dataSource).toBeInstanceOf(MatTreeNestedDataSource);
    expect(component.dataSource.data).toEqual(testData);
  });

  it('should change view after changes', () => {
    spyOn(component, 'ngAfterViewInit');
    component.dataSource.data = [
      {
        label: 'Foo',
        children: []
      },
      {
        label: 'Bar',
        children: []
      }
    ];
    // TODO
    expect(component.ngAfterViewInit).toHaveBeenCalled();
  });
});
