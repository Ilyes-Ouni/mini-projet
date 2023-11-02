import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Router, ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { RegionService } from 'src/app/regions/services/region.service';
import { StockDataService } from 'src/app/shared/services/stock-data.service';
import Swal from 'sweetalert2';
import { ClientService } from '../../services/client.service';

@Component({
  selector: 'app-update-form',
  templateUrl: './update-form.component.html',
  styleUrls: ['./update-form.component.css']
})
export class UpdateFormComponent {
  oldImagePath!: string;
  regions: Array<any> = [];
  file: File | null = null;
  dateCreation!: Date;
  clientID!: number;
  conferencesSubscription!: Subscription;
  addParticipantSubscription!: Subscription;
  constructor(private clientService: ClientService, private fb: FormBuilder, private stockDataService: StockDataService,
    private route: ActivatedRoute, private dialog: MatDialog, private regionService: RegionService) { }

  ngOnInit(): void {
    this.conferencesSubscription = this.regionService.getRegions()
      .subscribe((res: any) => {
        this.regions = res
      })

    this.stockDataService.id.subscribe((res: any) => {
      this.clientID = res
    })

    this.clientService.getClient(this.clientID)
      .subscribe(
        {
          next: (res: any) => {
            console.log(res);
            this.oldImagePath = res.imagePath
            this.clientID = res.idClient
            this.dateCreation = res.dateCreation

            this.ClientForm.setValue({
              nom: res.nom,
              email: res.email,
              phoneNumber: res.phoneNumber,
              regionID: res.region.idRegion,
              dateNaissance: res.dateNaissance.split('T')[0],
            })
          }
        }
      )
  }

  ClientForm = this.fb.group({
    nom: [, Validators.required],
    email: [, Validators.required],
    phoneNumber: [, Validators.required],
    regionID: [, Validators.required],
    dateNaissance: [, Validators.required],
  })


  UpdateCl(form: any) {
    if (this.ClientForm.status == 'VALID') {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

      if (emailRegex.test(form.email)) {       
        form.dateCreation = this.dateCreation
        if(!this.file) form.imagePath = this.oldImagePath
        this.addParticipantSubscription = this.clientService.updateClient(this.clientID, form.regionID, form)
          .subscribe({
            next: (res: any) => {
              if(this.file){
                this.clientService.setClientImage(this.file, this.clientID)
                  .subscribe({
                    next: (res) => {
                      this.dialog.closeAll();
                      Swal.fire({
                        icon: 'success',
                        title: 'Success...',
                        text: 'Client has been updated successfully !',
                      })
                    },
                    error: (res: any) => {
                      Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: 'Could not update this Client !',
                      })
                    }
                  })
                }else{
                  this.dialog.closeAll();
                  Swal.fire({
                    icon: 'success',
                    title: 'Success...',
                    text: 'Client has been updated successfully !',
                  })
                }
            }
          })
      }
    }
  }

  onFilechange(event: any) {
    this.file = event.target.files[0]
  }

  ngOnDestroy() {
    if (this.conferencesSubscription) this.conferencesSubscription.unsubscribe();
    if (this.addParticipantSubscription) this.addParticipantSubscription.unsubscribe();
  }
}
