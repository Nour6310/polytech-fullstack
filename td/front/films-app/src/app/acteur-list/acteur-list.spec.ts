import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActeurList } from './acteur-list';

describe('ActeurList', () => {
  let component: ActeurList;
  let fixture: ComponentFixture<ActeurList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ActeurList],
    }).compileComponents();

    fixture = TestBed.createComponent(ActeurList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
