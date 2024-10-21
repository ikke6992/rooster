import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { DataService } from '../data.service';
import { ModalComponent } from "../../modal/modal.component";

@Component({
  selector: 'app-set-group',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ModalComponent],
  templateUrl: './set-group.component.html',
  styleUrl: './set-group.component.css'
})
export class SetGroupComponent {
  @Input() groups: any[] = [];
  @Input() teacher: any;

  addGroup = new FormGroup({
    setGroup: new FormControl(),
  });
errorMsg!: string;

  constructor(private dataService: DataService) {}

  onSubmit() {
    const data = this.addGroup.value;
    console.log(data);
    this.dataService.putGroup(this.teacher.id, data.setGroup).subscribe(
      (response) => {
        console.log('Response:', response);
        window.location.reload();
      },
      (error) => {
        console.error('Error:', error);
        this.errorMsg = error.error;
        this.showModal('error-'+this.teacher.name);
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
