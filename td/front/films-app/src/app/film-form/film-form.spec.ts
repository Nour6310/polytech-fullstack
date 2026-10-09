import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FilmForm } from './film-form';

describe('FilmForm', () => {
  let component: FilmForm;
  let fixture: ComponentFixture<FilmForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FilmForm],
    }).compileComponents();

    fixture = TestBed.createComponent(FilmForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
