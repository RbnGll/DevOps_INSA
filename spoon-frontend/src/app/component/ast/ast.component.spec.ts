import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {AstComponent, ASTNode} from './ast.component';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {NestedTreeControl} from '@angular/cdk/tree';
import {MatTreeNestedDataSource} from '@angular/material/tree';
import {CommandsRegistry} from 'interacto';
import {UpdateCode} from '../../command/update-code';

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

  it('hasChild should be true if element have child', () => {
    const data: ASTNode = {
      label : 'Foo',
      children : [ {label : 'bar'}]
    };
    expect(component.hasChild(0, data)).toBeTrue();
  });

  it('hasChild should be false if element have no child', () => {
    const data: ASTNode = {
      label : 'Foo'
    };
    expect(component.hasChild(0, data)).toBeFalse();
  });

  it('should update code when writing in the TextInput', () => {
    jasmine.clock().install();
    component.getCode().nativeElement.value = 'f';
    component.getCode().nativeElement.dispatchEvent(new InputEvent('input'));
    component.getCode().nativeElement.value = 'fo';
    component.getCode().nativeElement.dispatchEvent(new InputEvent('input'));
    component.getCode().nativeElement.value = 'foo';
    component.getCode().nativeElement.dispatchEvent(new InputEvent('input'));
    jasmine.clock().tick(1000);
    expect(component.getCode().nativeElement.value).toEqual('foo');

    expect(CommandsRegistry.getInstance().getCommands().length).toEqual(1);
    expect(CommandsRegistry.getInstance().getCommands()[0]).toBeInstanceOf(UpdateCode);
    jasmine.clock().uninstall();
  });
});
