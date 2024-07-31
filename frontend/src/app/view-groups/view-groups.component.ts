import { Component } from '@angular/core';
import { DataService } from './data.service';
import { ModalComponent } from '../modal/modal.component';
import { AddGroupComponent } from './add-group/add-group.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-view-groups',
  standalone: true,
  imports: [CommonModule, ModalComponent, AddGroupComponent],
  templateUrl: './view-groups.component.html',
  styleUrl: './view-groups.component.css'
})
export class ViewGroupsComponent {
  data: any[] = [];

  constructor(private dataService: DataService) {}

  ngOnInit(): void {
    this.dataService.getData().subscribe((response: any[]) => {
      this.data = response;
    });

    
  }

  showModal() {
    let modal_t = document.getElementById('add-group');
    if (modal_t !== null) {
      modal_t.classList.remove('hhidden');
      modal_t.classList.add('sshow');
    }
  }
  closeModal() {
    let modal_t = document.getElementById('add-group');
    if (modal_t !== null) {
      modal_t.classList.remove('sshow');
      modal_t.classList.add('hhidden');
    }
  }
}
