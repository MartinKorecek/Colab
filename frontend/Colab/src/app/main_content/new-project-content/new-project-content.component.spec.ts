import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewProjectContentComponent } from './new-project-content.component';

describe('NewProjectContentComponent', () => {
  let component: NewProjectContentComponent;
  let fixture: ComponentFixture<NewProjectContentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewProjectContentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewProjectContentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
