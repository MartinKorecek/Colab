import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LogoutContentComponent } from './logout-content.component';

describe('LogoutContentComponent', () => {
  let component: LogoutContentComponent;
  let fixture: ComponentFixture<LogoutContentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LogoutContentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LogoutContentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
