package com.example.market.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {

    private List<TicketDTO> tickets = new ArrayList<>();
    private double totalAccumulated;

    public void addTicket(TicketDTO t){
        this.tickets.add(t);
    }

    public boolean emptyCart(){
        return tickets.isEmpty();
    }
}
