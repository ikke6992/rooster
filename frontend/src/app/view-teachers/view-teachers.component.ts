import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ModalComponent } from '../modal/modal.component';
import { DataService } from './data.service';
import { SetAvailabilityComponent } from './set-availability/set-availability.component';
import { AddTeacherComponent } from './add-teacher/add-teacher.component';
import { SetGroupComponent } from './set-group/set-group.component';

@Component({
  selector: 'app-view-teachers',
  standalone: true,
  imports: [CommonModule, ModalComponent, SetAvailabilityComponent, SetGroupComponent, AddTeacherComponent],
  templateUrl: './view-teachers.component.html',
  styleUrl: './view-teachers.component.css',
})
export class ViewTeachersComponent {
  groups: any[] = [];
  teachers: any[] = [];

  constructor(private dataService: DataService) {}

  ngOnInit(): void {
    this.dataService.getTeachers().subscribe((response: any[]) => {
      this.teachers = response;
      console.log(response);
    });

    this.dataService.getGroups().subscribe((response: any[]) => {
      this.groups = response.filter((group) => group.groupNumber !== 0);
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
