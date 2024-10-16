import { Component, Input } from '@angular/core';
import { DataService } from '../data.service';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ModalComponent } from '../../modal/modal.component';

@Component({
  selector: 'app-edit-field',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ModalComponent],
  templateUrl: './edit-field.component.html',
  styleUrl: './edit-field.component.css',
})
export class EditFieldComponent {
  feedbackMsg!: string;
  window = window;

  @Input() field: any;

  editField!: FormGroup;

  constructor(private dataService: DataService) {}

  ngOnInit() {
    this.initializeForm();
  }

  ngOnChanges(changes: any) {
    if (changes.teacher && !changes.teacher.firstChange) {
      this.initializeForm();
    }
  }

  private initializeForm() {
    this.editField = new FormGroup({
      name: new FormControl(this.field.name),
      daysPhase1: new FormControl(this.field.daysPhase1),
      daysPhase2: new FormControl(this.field.daysPhase2),
      daysPhase3: new FormControl(this.field.daysPhase3),
    });
  }

  onSubmit() {
    const data = this.editField.value;
    this.dataService.putField(this.field.id, data).subscribe(
      (response) => {
        console.log('Response:', response);
        this.feedbackMsg = `Field ${response.name} succesfully edited`;
        this.showModal('feedback');
      },
      (error) => {
        console.error('Error:', error);
        this.feedbackMsg = `Error: ${error.error}`;
        this.showModal('feedback');
      }
    );
  }

  showModal(name: string) {
    let modal_t = document.getElementById(name);
    if (modal_t !== null) {
      modal_t.classList.remove('hhidden');
      modal_t.classList.add('sshow');
    }
  }

  closeModal(name: string) {
    let modal_t = document.getElementById(name);
    if (modal_t !== null) {
      modal_t.classList.remove('sshow');
      modal_t.classList.add('hhidden');
    }
  }
}
