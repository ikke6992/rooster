import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ModalComponent } from '../modal/modal.component';
import { DataService } from './data.service';
import { SetAvailabilityComponent } from './set-availability/set-availability.component';

@Component({
  selector: 'app-view-teachers',
  standalone: true,
  imports: [CommonModule, ModalComponent, SetAvailabilityComponent],
  templateUrl: './view-teachers.component.html',
  styleUrl: './view-teachers.component.css',
})
export class ViewTeachersComponent {
  teachers: any[] = [];

  constructor(private dataService: DataService) {}

  ngOnInit(): void {
    this.dataService.getTeachers().subscribe((response: any[]) => {
      this.teachers = response;
      console.log(response);
    });
  }

  showModal() {
    let modal_t = document.getElementById('set-availability');
    if (modal_t !== null) {
      modal_t.classList.remove('hhidden');
      modal_t.classList.add('sshow');
    }
  }
  closeModal() {
    let modal_t = document.getElementById('set-availability');
    if (modal_t !== null) {
      modal_t.classList.remove('sshow');
      modal_t.classList.add('hhidden');
    }
  }
}
