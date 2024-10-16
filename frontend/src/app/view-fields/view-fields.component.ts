import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ModalComponent } from '../modal/modal.component';
import { DataService } from './data.service';
import { AddFieldComponent } from './add-field/add-field.component';

@Component({
  selector: 'app-view-fields',
  standalone: true,
  imports: [CommonModule, ModalComponent, AddFieldComponent],
  templateUrl: './view-fields.component.html',
  styleUrl: './view-fields.component.css',
})
export class ViewFieldsComponent {
  fields: any[] = [];

  constructor(private dataService: DataService) {}

  ngOnInit(): void {
    this.dataService.getFields().subscribe((response: any[]) => {
      this.fields = response.filter((field) => field.name !== 'Returnday');
      console.log(response);
    });
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
