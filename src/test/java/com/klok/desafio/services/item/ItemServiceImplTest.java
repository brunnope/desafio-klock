package com.klok.desafio.services.item;

import com.klok.desafio.entities.Item;
import com.klok.desafio.entities.Pedido;
import com.klok.desafio.exceptions.BusinessRuleException;
import com.klok.desafio.exceptions.ResourceNotFoundException;
import com.klok.desafio.repositories.ItemRepository;
import com.klok.desafio.services.item.utils.ItemValidator;
import com.klok.desafio.services.pedido.PedidoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemValidator itemValidator;

    @Mock
    private PedidoService pedidoService;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    @DisplayName("Deve listar itens com sucesso")
    void listarItens_comSucesso() {

        Item item1 = new Item();
        Item item2 = new Item();

        when(itemRepository.findAll()).thenReturn(List.of(item1, item2));

        List<Item> itens = itemService.listarItens();

        assertNotNull(itens);
        assertEquals(2, itens.size());

        verify(itemRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve lançar BusinessRuleException quando não houver itens cadastrados")
    void listarItens_quandoNaoHouverItens() {

        when(itemRepository.findAll()).thenReturn(List.of());

        assertThrows(BusinessRuleException.class, () -> itemService.listarItens());

        verify(itemRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve buscar item por ID com sucesso")
    void buscarItemPorId_comSucesso() {

        Item item = new Item();
        item.setId(1L);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        Item resultado = itemService.buscarItemPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());

        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar item inexistente por ID")
    void buscarItemPorId_quandoNaoEncontrar() {

        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> itemService.buscarItemPorId(1L));

        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve salvar item com sucesso")
    void salvarItem_comSucesso() {

        Item item = new Item();
        item.setNome("Item Teste");

        doNothing().when(itemValidator).validarItem(item);
        when(itemRepository.save(item)).thenReturn(item);

        Item resultado = itemService.salvarItem(item);

        assertNotNull(resultado);
        assertEquals("Item Teste", resultado.getNome());

        verify(itemValidator, times(1)).validarItem(item);
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    @DisplayName("Deve lançar BusinessRuleException ao tentar salvar item nulo")
    void salvarItem_quandoItemForNulo() {

        assertThrows(BusinessRuleException.class, () -> itemService.salvarItem(null));

        verifyNoInteractions(itemValidator);
        verifyNoInteractions(itemRepository);
    }

    @Test
    @DisplayName("Deve atualizar item com sucesso")
    void atualizarItem_comSucesso() {

        Item itemAtualizado = new Item();
        itemAtualizado.setNome("Nome Atualizado");

        Item itemExistente = new Item();
        itemExistente.setNome("Nome Original");

        when(itemRepository.getReferenceById(1L)).thenReturn(itemExistente);
        when(itemRepository.save(itemExistente)).thenReturn(itemExistente);

        Item resultado = itemService.atualizarItem(1L, itemAtualizado);

        assertNotNull(resultado);
        assertEquals("Nome Atualizado", resultado.getNome());

        verify(itemRepository, times(1)).getReferenceById(1L);
        verify(itemRepository, times(1)).save(itemExistente);
    }

    @Test
    @DisplayName("Deve permanecer o nome do item igual")
    void atualizarItemComFalha_quandoNomeNaoMudar() {

        Item itemAtualizado = new Item();

        Item itemExistente = new Item();
        itemExistente.setNome("Nome Original");

        when(itemRepository.getReferenceById(1L)).thenReturn(itemExistente);
        when(itemRepository.save(itemExistente)).thenReturn(itemExistente);

        Item resultado = itemService.atualizarItem(1L, itemAtualizado);

        assertNotNull(resultado);
        assertEquals("Nome Original", resultado.getNome());

        verify(itemRepository, times(1)).getReferenceById(1L);
        verify(itemRepository, times(1)).save(itemExistente);
    }

    @Test
    @DisplayName("Deve excluir item com sucesso")
    void excluirItem_comSucesso() {

        Item item = new Item();
        Pedido pedido = new Pedido();
        item.setPedido(pedido);
        pedido.getItens().add(item);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        itemService.excluirItem(1L);

        assertTrue(pedido.getItens().isEmpty());
        assertNull(item.getPedido());

        verify(itemRepository, times(1)).findById(1L);
        verify(pedidoService, times(1)).salvarPedido(pedido);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao tentar excluir item inexistente")
    void excluirItem_quandoNaoEncontrar() {

        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> itemService.excluirItem(1L));

        verify(itemRepository, times(1)).findById(1L);
        verifyNoInteractions(pedidoService);
    }
}