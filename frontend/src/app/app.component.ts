import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { DataService } from './data.service';
import { AddGroupComponent } from './add-group/add-group.component';
import { ModalComponent } from './modal/modal.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, AddGroupComponent, ModalComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent implements OnInit {
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
