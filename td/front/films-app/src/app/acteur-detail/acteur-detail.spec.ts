import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActeurDetail } from './acteur-detail';

describe('ActeurDetail', () => {
  let component: ActeurDetail;
  let fixture: ComponentFixture<ActeurDetail>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ActeurDetail],
    }).compileComponents();

    fixture = TestBed.createComponent(ActeurDetail);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
