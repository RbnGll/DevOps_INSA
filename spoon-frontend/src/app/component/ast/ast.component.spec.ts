import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AstComponent } from './ast.component';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('AstComponent', () => {
  let component: AstComponent;
  let fixture: ComponentFixture<AstComponent>;

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

});
