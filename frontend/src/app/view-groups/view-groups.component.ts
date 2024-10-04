import { Component } from '@angular/core';
import { DataService } from './data.service';
import { ModalComponent } from '../modal/modal.component';
import { AddGroupComponent } from './add-group/add-group.component';
import { CommonModule } from '@angular/common';
import { AddVacationComponent } from './add-vacation/add-vacation.component';

@Component({
  selector: 'app-view-groups',
  standalone: true,
  imports: [
    CommonModule,
    ModalComponent,
    AddGroupComponent,
    AddVacationComponent,
  ],
  templateUrl: './view-groups.component.html',
  styleUrl: './view-groups.component.css',
})
export class ViewGroupsComponent {
  groups: any[] = [];
  fields: any[] = [];

  constructor(private dataService: DataService) {}

  ngOnInit(): void {
    this.dataService.getGroups().subscribe((response: any[]) => {
      this.groups = response;
      console.log(response);
    });

    this.dataService.getFields().subscribe((response: any[]) => {
      this.fields = response.filter((field) => field.name !== 'Returnday');
      console.log(response);
    });
  }

  reschedule(number: number) {
    if (window.confirm('Reschedule Group ' + number + '?')) {
      this.dataService.rescheduleGroup(number).subscribe(
        (response) => {
          console.log('Response:', response);
          window.location.reload();
        },
        (error) => {
          console.error('Error:', error);
        }
      );
    }
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
